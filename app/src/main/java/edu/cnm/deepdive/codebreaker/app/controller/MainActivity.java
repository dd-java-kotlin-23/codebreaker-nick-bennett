package edu.cnm.deepdive.codebreaker.app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.adapter.GuessListAdapter;
import edu.cnm.deepdive.codebreaker.app.databinding.ActivityMainBinding;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;
import edu.cnm.deepdive.codebreaker.model.Game;
import jakarta.inject.Inject;
import java.util.regex.Pattern;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  @Inject
  GuessListAdapter adapter;

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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.game_options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled;
    if (item.getItemId() == R.id.new_game) {
      startGame();
      handled = true;
    } else if (item.getItemId() == R.id.settings) {
      Intent intent = new Intent(this, SettingsActivity.class);
      startActivity(intent);
      handled = true;
    } else {
      handled = super.onOptionsItemSelected(item);
    }
    return handled;
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
    binding.guessList.setAdapter(adapter);
  }

  private void setupViewModel() {
    viewModel = new ViewModelProvider(this).get(GameViewModel.class);
    viewModel.getGame().observe(this, this::handleGame);
    viewModel.getSolved().observe(this, this::handleSolved);
    viewModel.getShowText().observe(this, this::handleShowText);
    viewModel.getError().observe(this, this::handleError);
  }

  private void handleShowText(boolean showText) {
    adapter.setShowText(showText);
    binding.guessListHeader.guessText.setVisibility(showText ? View.VISIBLE : View.GONE);
    binding.guessList.postInvalidate();
  }

  private void attachButtonListeners() {
    binding.submitGuess.setOnClickListener((_) -> submitGuess());
  }

  private void handleGame(Game game) {
    if (!game.equals(this.game)) {
      adapter.clear();
    }
    this.game = game;
    updateGameDisplay();
    attachGuessListeners();
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
    binding.pool.setText(getString(R.string.pool_format, game.pool()));
    binding.length.setText(getString(R.string.length_format, game.length()));
    adapter.addAll(game.guesses().subList(adapter.getCount(), game.guesses().size()));
    binding.waitingIndicator.setVisibility(View.GONE);
  }

  private void attachGuessListeners() {
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

  private void startGame() {
    disableGameControls();
    binding.guessInput.setText("");
    viewModel.startGame();
  }

  private void submitGuess() {
    disableGameControls();
    //noinspection DataFlowIssue
    viewModel.submitGuess(binding.guessInput.getText().toString());
  }

  private void disableGameControls() {
    binding.submitGuess.setEnabled(false);
    binding.guessInput.setEnabled(false);
    binding.waitingIndicator.setVisibility(View.VISIBLE);
  }

  private void updateGuessControls() {
    if (solved || game == null) {
      binding.guessInput.setEnabled(false);
      binding.submitGuess.setEnabled(false);
    } else {
      binding.guessInput.setEnabled(true);
      binding.submitGuess.setEnabled(guessReady);
      binding.guessInput.post(() -> binding.guessInput.requestFocus());
    }
  }

  private record GuessPoolFilter(Pattern filter) implements InputFilter {

    private static final String FILTER_PATTERN_FORMAT = "[^%s]+";

    private GuessPoolFilter(String pool) {
      this(Pattern.compile(FILTER_PATTERN_FORMAT.formatted(pool)));
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