package edu.cnm.deepdive.codebreaker.app.viewmodel;

import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.codebreaker.app.repository.PreferencesRepository;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.service.CodebreakerService;
import jakarta.inject.Inject;

@HiltViewModel
public class GameViewModel extends ViewModel {

  private static final String TAG = GameViewModel.class.getSimpleName();
  private static final String MASTER_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private final CodebreakerService service;
  private final PreferencesRepository repository;
  private final MutableLiveData<Game> game;
  private final LiveData<Boolean> solved;
  private final MutableLiveData<Throwable> error;
  private final Observer<Integer> codeLengthObserver = (length) -> setCodeLength(length);
  private final Observer<Integer> poolSizeObserver = (size) -> setPoolSize(size);

  private int codeLength;
  private int poolSize;
  private boolean gameStarted;

  @Inject
  GameViewModel(CodebreakerService service, PreferencesRepository repository) {
    this.service = service;
    this.repository = repository;
    game = new MutableLiveData<>();
    solved = Transformations.map(game, Game::isSolved);
    error = new MutableLiveData<>();
    repository.getCodeLength().observeForever(codeLengthObserver);
    repository.getPoolSize().observeForever(poolSizeObserver);
  }

  @Override
  protected void onCleared() {
    repository.getCodeLength().removeObserver(codeLengthObserver);
    repository.getPoolSize().removeObserver(poolSizeObserver);
    super.onCleared();
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

  public void startGame() {
    error.postValue(null);
    service
        .startGame(MASTER_POOL.substring(0, poolSize), codeLength)
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

  private void setCodeLength(int length) {
    codeLength = length;
    checkGameStarted();
  }

  private void setPoolSize(int size) {
    poolSize = size;
    checkGameStarted();
  }

  private void checkGameStarted() {
    if (!gameStarted && codeLength > 0 && poolSize > 0) {
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
