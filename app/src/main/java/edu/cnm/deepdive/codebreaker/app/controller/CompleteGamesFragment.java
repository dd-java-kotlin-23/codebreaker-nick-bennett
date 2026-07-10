package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentCompleteGamesBinding;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;

@AndroidEntryPoint
public class CompleteGamesFragment extends Fragment {

  private FragmentCompleteGamesBinding binding;
  private GameViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentCompleteGamesBinding.inflate(inflater, container, false);
    // TODO: 7/10/26 Attach event listeners.
    binding.codeLength.setOnSeekBarChangeListener((SeekBarOnlyChangeListener) (_, value, byUser) ->
        binding.codeLengthValue.setText(String.valueOf(value)));
    binding.poolSize.setOnSeekBarChangeListener((SeekBarOnlyChangeListener) (_, value, byUser) ->
        binding.poolSizeValue.setText(String.valueOf(value)));
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    // TODO: 7/9/26 Connect to the view model; attach observers; handle other lifecycle-aware
    //  operations.
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    // TODO: 7/9/26 Set binding field to null; perform other cleanup, as necessary.
    super.onDestroyView();
  }

  @FunctionalInterface
  private interface SeekBarOnlyChangeListener extends OnSeekBarChangeListener {

    @Override
    default void onStartTrackingTouch(SeekBar seekBar) {
      // Do nothing.
    }

    @Override
    default void onStopTrackingTouch(SeekBar seekBar) {
      // Do nothing.
    }

  }

}
