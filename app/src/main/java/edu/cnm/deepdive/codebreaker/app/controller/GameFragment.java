package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.adapter.GuessListAdapter;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentGameBinding;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;
import edu.cnm.deepdive.codebreaker.model.Game;
import jakarta.inject.Inject;
import java.util.regex.Pattern;

@AndroidEntryPoint
public class GameFragment extends Fragment implements MenuProvider {

  @Inject
  GuessListAdapter adapter;

  private FragmentGameBinding binding;
  private GameViewModel viewModel;
  private boolean guessReady;
  private boolean solved;
  private Game game;
  private TextWatcher guessReadyWatcher;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    setupLayout(inflater, container);
    attachButtonListeners();
    updateGuessControls();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViewModel();
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  @Override
  public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.game_options, menu);
  }

  @Override
  public boolean onMenuItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    if (item.getItemId() == R.id.new_game) {
      startGame();
    } else if (item.getItemId() == R.id.settings) {
      showSettings();
    } else {
      handled = false;
    }
    return handled;
  }

  private void setupLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
    binding = FragmentGameBinding.inflate(inflater, container, false);
    binding.guessList.setAdapter(adapter);
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

  private void showSettings() {
    NavController navController = Navigation.findNavController(binding.getRoot());
    navController.navigate(GameFragmentDirections.showSettings());
  }

  private void setupViewModel() {
    FragmentActivity activity = requireActivity();
    viewModel = new ViewModelProvider(activity).get(GameViewModel.class);
    LifecycleOwner viewLifecycleOwner = getViewLifecycleOwner();
    viewModel.getGame().observe(viewLifecycleOwner, this::handleGame);
    viewModel.getSolved().observe(viewLifecycleOwner, this::handleSolved);
    viewModel.getShowText().observe(viewLifecycleOwner, this::handleShowText);
    viewModel.getError().observe(viewLifecycleOwner, this::handleError);
    activity.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED);
  }

  private void attachButtonListeners() {
    binding.submitGuess.setOnClickListener((_) -> submitGuess());
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

  private void handleGame(Game game) {
    if (!game.equals(this.game)) {
      adapter.clear();
    }
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

  private void handleShowText(Boolean show) {
    adapter.setShowText(show);
    binding.guessListHeader.guessText.setVisibility(show ? View.VISIBLE : View.GONE);
    binding.guessList.postInvalidate();
  }

  private void handleError(Throwable error) {
    if (error != null) {
      Snackbar.make(binding.getRoot(),
              getString(R.string.snackbar_error_format, error),
              Snackbar.LENGTH_LONG)
          .show();
    }
  }

  private void disableGameControls() {
    binding.submitGuess.setEnabled(false);
    binding.guessInput.setEnabled(false);
    binding.waitingIndicator.setVisibility(View.VISIBLE);
  }

  private void updateGameDisplay() {
    binding.pool.setText(getString(R.string.pool_format, game.pool()));
    binding.length.setText(getString(R.string.length_format, game.length()));
    adapter.addAll(game.guesses().subList(adapter.getCount(), game.guesses().size()));
    binding.waitingIndicator.setVisibility(View.GONE);
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
