package edu.cnm.deepdive.codebreaker.app.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.repository.PreferencesRepository;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.service.CodebreakerService;
import jakarta.inject.Inject;

@HiltViewModel
public class GameViewModel extends ViewModel {

  private static final String TAG = GameViewModel.class.getSimpleName();

  private final CodebreakerService service;
  private final PreferencesRepository repository;
  private final MutableLiveData<Game> game;
  private final LiveData<Boolean> solved;
  private final MutableLiveData<Throwable> error;
  private final Observer<Integer> codeLengthObserver = this::setCodeLength;
  private final Observer<Integer> poolSizeObserver = this::setPoolSize;
  private final String masterPool;

  private int codeLength;
  private int poolSize;
  private boolean gameStarted;

  @Inject
  GameViewModel(@ApplicationContext Context context, CodebreakerService service,
      PreferencesRepository repository) {
    this.service = service;
    this.repository = repository;
    game = new MutableLiveData<>();
    solved = Transformations.map(game, Game::isSolved);
    error = new MutableLiveData<>();
    masterPool = context.getString(R.string.master_pool);
    repository.getCodeLength().observeForever(codeLengthObserver);
    repository.getPoolSize().observeForever(poolSizeObserver);
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Boolean> getSolved() {
    return Transformations.distinctUntilChanged(solved);
  }

  public LiveData<Boolean> getShowText() {
    return Transformations.distinctUntilChanged(repository.getShowText());
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  @Override
  protected void onCleared() {
    repository.getCodeLength().removeObserver(codeLengthObserver);
    repository.getPoolSize().removeObserver(poolSizeObserver);
    super.onCleared();
  }

  public void startGame() {
    error.setValue(null);
    service
        .startGame(masterPool.substring(0, poolSize), codeLength)

        .thenAccept(game::postValue)
        .exceptionally(this::postError);
  }

  public void submitGuess(String text) {
    error.setValue(null);
    service
        .submitGuess(game.getValue(), text)
        .thenAccept(game::postValue)
        .exceptionally(this::postError);
  }

  private void setCodeLength(Integer codeLength) {
    this.codeLength = codeLength;
    checkGameStarted();
  }

  private void setPoolSize(Integer poolSize) {
    this.poolSize = poolSize;
    checkGameStarted();
  }

  private void checkGameStarted() {
    if (!gameStarted && codeLength > 0 && poolSize > 0) {
      gameStarted = true;
      startGame();
    }
  }

  @Nullable
  private Void postError(Throwable error) {
    Log.e(TAG, error.getMessage(), error);
    this.error.postValue(error);
    return null;
  }

}
