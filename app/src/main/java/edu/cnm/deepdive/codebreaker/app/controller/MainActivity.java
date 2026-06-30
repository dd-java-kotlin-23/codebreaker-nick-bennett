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
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.databinding.ActivityMainBinding;
import edu.cnm.deepdive.codebreaker.model.Game;
import java.util.regex.Pattern;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private boolean guessReady;
  private boolean solved;
  private TextWatcher guessReadyWatcher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
  }

  private void updateGameDisplay(Game game) {
    // TODO: 6/30/26 Update list views, status indicators, etc.
    binding.gameState.setText(game.toString());
  }

  private void setupGuessListeners(Game game) {
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
    if (solved) {
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