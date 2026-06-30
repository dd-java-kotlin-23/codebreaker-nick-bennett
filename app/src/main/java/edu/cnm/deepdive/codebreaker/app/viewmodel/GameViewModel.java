package edu.cnm.deepdive.codebreaker.app.viewmodel;

import android.util.Log;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.service.CodebreakerService;
import jakarta.inject.Inject;

@HiltViewModel
public class GameViewModel extends ViewModel {

  private static final String TAG = GameViewModel.class.getSimpleName();

  private final CodebreakerService service;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Throwable> error;

  @Inject
  public GameViewModel(CodebreakerService service) {
    this.service = service;
    game = new MutableLiveData<>();
    error = new MutableLiveData<>();
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Throwable> getError() {
    return error;
  }

  public void startGame() {
    error.setValue(null);
    service
        .startGame("ABCDEF", 3)
        .thenAccept(game::postValue)
        .exceptionally(this::postError);
  }

  @Nullable
  private Void postError(Throwable error) {
    Log.e(TAG, error.getMessage(), error);
    this.error.postValue(error);
    return null;
  }

}
