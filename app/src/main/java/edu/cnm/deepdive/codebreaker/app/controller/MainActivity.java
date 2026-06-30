package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.databinding.ActivityMainBinding;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;
import edu.cnm.deepdive.codebreaker.model.Game;
import java.util.regex.Pattern;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private GameViewModel viewModel;
  private boolean guessReady;
  private boolean solved;
  private Game game;
  private TextWatcher guessReadyWatcher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupLayout();
    setupViewModel();
    attachButtonListeners();
    updateGuessControls();
  }

  private void setupLayout() {
    EdgeToEdge.enable(this);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
  }

  private void setupViewModel() {
    viewModel = new ViewModelProvider(this).get(GameViewModel.class);
    viewModel
        .getGame()
        .observe(this, this::handleGame);
    viewModel
        .getSolved()
        .observe(this, this::handleSolved);
    viewModel
        .getError()
        .observe(this, this::handleError);
  }

  private void attachButtonListeners() {
    binding.startGame.setOnClickListener((_) -> viewModel.startGame());
    //noinspection DataFlowIssue
    binding.submitGuess.setOnClickListener((_) ->
        viewModel.submitGuess(binding.guessInput.getText().toString()));
  }

  private void handleGame(Game game) {
    this.game = game;
    updateGameDisplay();
    setupGuessListeners();
    updateGuessControls();
  }

  private void handleSolved(boolean solved) {
    this.solved = solved;
    updateGuessControls();
    if (solved) {
      Snackbar.make(binding.getRoot(),
              getString(R.string.solved_message_format, game.getCode(), game.guesses().size()),
              Snackbar.LENGTH_LONG)
          .show();
    }
  }

  private void handleError(Throwable error) {
    if (error != null) {
      Snackbar.make(binding.getRoot(),
              getString(R.string.snackbar_error_format, error),
              Snackbar.LENGTH_LONG)
          .show();
    }
  }

  private void updateGameDisplay() {
    // TODO: 6/30/26 Update list views, status indicators, etc.
    binding.gameState.setText(game.toString());
  }

  private void setupGuessListeners() {
    //noinspection DataFlowIssue
    binding
        .guessInput
        .getText()
        .setFilters(new InputFilter[]{
            new InputFilter.AllCaps(),
            new GuessPoolFilter(game.pool()),
            new InputFilter.LengthFilter(game.length()),
        });
    if (guessReadyWatcher != null) {
      binding.guessInput.removeTextChangedListener(guessReadyWatcher);
    }
    guessReadyWatcher = new GuessReadyWatcher(game.length());
    binding
        .guessInput
        .addTextChangedListener(guessReadyWatcher);
  }

  private void updateGuessControls() {
    if (solved || game == null) {
      binding.guessInput.setEnabled(false);
      binding.submitGuess.setEnabled(false);
    } else {
      binding.guessInput.setEnabled(true);
      binding.submitGuess.setEnabled(guessReady);
    }
  }

  private static class GuessPoolFilter implements InputFilter {

    private static final String FILTER_PATTERN_FORMAT = "[^%s]+";
    private final Pattern filter;

    private GuessPoolFilter(String pool) {
      filter = Pattern.compile(FILTER_PATTERN_FORMAT.formatted(pool));
    }

    @Override
    public CharSequence filter(CharSequence source, int srcStart, int srcEnd,
        Spanned destination, int destStart, int destEnd) {
      return filter.matcher(source.subSequence(srcStart, srcEnd)).replaceAll("");
    }

  }

  private class GuessReadyWatcher implements TextWatcher {

    private final int codeLength;

    private GuessReadyWatcher(int codeLength) {
      this.codeLength = codeLength;
    }

    @Override
    public void afterTextChanged(Editable editable) {
      guessReady = editable.codePoints().count() == codeLength;
      updateGuessControls();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
      // Do nothing.
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
      // Do nothing.
    }

  }

}