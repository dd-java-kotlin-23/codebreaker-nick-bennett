package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    getMenuInflater().inflate(R.menu.game_options, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.new_game) {
      binding.guessInput.setText("");
      viewModel.startGame();
      return true;
    }
    return super.onOptionsItemSelected(item);
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
    binding.pool.setText(getString(R.string.pool_format, game.pool()));
    binding.length.setText(getString(R.string.length_format, game.length()));
    binding.guessList.setAdapter(
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, game.guesses()));
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