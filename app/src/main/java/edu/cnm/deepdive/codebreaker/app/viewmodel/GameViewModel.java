package edu.cnm.deepdive.codebreaker.app.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.model.entity.CompleteGame;
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame;
import edu.cnm.deepdive.codebreaker.app.repository.GameRepository;
import edu.cnm.deepdive.codebreaker.app.repository.PreferencesRepository;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.service.CodebreakerService;
import jakarta.inject.Inject;
import java.util.List;

@HiltViewModel
public class GameViewModel extends ViewModel {

  private static final String TAG = GameViewModel.class.getSimpleName();

  private final CodebreakerService service;
  private final PreferencesRepository preferencesRepository;
  private final GameRepository gameRepository;
  private final MutableLiveData<Game> game = new MutableLiveData<>();
  private final LiveData<Boolean> solved = Transformations.map(game, Game::isSolved);
  private final MutableLiveData<Integer> codeLength = new MutableLiveData<>();
  private final MutableLiveData<Integer> poolSize = new MutableLiveData<>();
  private final LiveData<List<CompleteGame>> completeGames = buildCompleteGameLiveData();
  private final MutableLiveData<Throwable> error = new MutableLiveData<>();
  private final Observer<Integer> codeLengthObserver = this::setCodeLengthPreference;
  private final Observer<Integer> poolSizeObserver = this::setPoolSizePreference;
  private final String masterPool;

  private int codeLengthPreference;
  private int poolSizePreference;
  private boolean gameStarted;

  @Inject
  GameViewModel(@ApplicationContext Context context, CodebreakerService service,
      PreferencesRepository preferencesRepository, GameRepository gameRepository) {
    this.service = service;
    this.preferencesRepository = preferencesRepository;
    this.gameRepository = gameRepository;
    masterPool = context.getString(R.string.master_pool);
    preferencesRepository.getCodeLength().observeForever(codeLengthObserver);
    preferencesRepository.getPoolSize().observeForever(poolSizeObserver);
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Boolean> getSolved() {
    return Transformations.distinctUntilChanged(solved);
  }

  public LiveData<Boolean> getShowText() {
    return Transformations.distinctUntilChanged(preferencesRepository.getShowText());
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength.setValue(codeLength);
  }

  public void setPoolSize(int poolSize) {
    this.poolSize.setValue(poolSize);
  }

  @Override
  protected void onCleared() {
    preferencesRepository.getCodeLength().removeObserver(codeLengthObserver);
    preferencesRepository.getPoolSize().removeObserver(poolSizeObserver);
    super.onCleared();
  }

  public void startGame() {
    error.setValue(null);
    service
        .startGame(masterPool.substring(0, poolSizePreference), codeLengthPreference)
        .thenCompose(gameRepository::save)
        .thenAccept(game::postValue)
        .exceptionally(this::postError);
  }

  public void submitGuess(String text) {
    error.setValue(null);
    service
        .submitGuess(game.getValue(), text)
        .thenCompose(gameRepository::save)
        .thenAccept(game::postValue)
        .exceptionally(this::postError);
  }

  public void getGame(String gameId) {
    error.setValue(null);
    service
        .getGame(gameId)
        .thenCompose(gameRepository::save)
        .thenAccept(game::postValue)
        .exceptionally(this::postError); // TODO: 7/8/26 Handle non-existent game.
  }

  public void deleteGame(String gameId) {
    error.setValue(null);
    gameRepository
        .delete(gameId)
        .thenCompose((_) -> service.deleteGame(gameId))
        .exceptionally(this::postError); // TODO: 7/8/26 How to indicate non-existent game.
  }

  public LiveData<List<IncompleteGame>> getIncompleteGames() {
    return gameRepository.getAll();
  }

  public LiveData<List<CompleteGame>> getCompleteGames() {
    return completeGames;
  }

  private void setCodeLengthPreference(Integer codeLengthPreference) {
    this.codeLengthPreference = codeLengthPreference;
    checkGameStarted();
  }

  private void setPoolSizePreference(Integer poolSizePreference) {
    this.poolSizePreference = poolSizePreference;
    checkGameStarted();
  }

  private void checkGameStarted() {
    if (!gameStarted && codeLengthPreference > 0 && poolSizePreference > 0) {
      gameStarted = true;
      startGame();
    }
  }

  private LiveData<List<CompleteGame>> buildCompleteGameLiveData() {
    MediatorLiveData<QueryCriteria> criteria = new MediatorLiveData<>();
    criteria.addSource(codeLength, (length) ->
        criteria.setValue(new QueryCriteria(length, poolSize.getValue())));
    criteria.addSource(poolSize, (size) ->
        criteria.setValue(new QueryCriteria(codeLength.getValue(), size)));
    return Transformations.switchMap(criteria, (crit) ->
        gameRepository.get(crit.codeLength(), crit.poolSize()));
  }

  @Nullable
  private Void postError(Throwable error) {
    Log.e(TAG, error.getMessage(), error);
    this.error.postValue(error);
    return null;
  }

  private record QueryCriteria(int codeLength, int poolSize) {}

}
