package edu.cnm.deepdive.codebreaker.app.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.adapter.CompleteGameAdapter;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentCompleteGamesBinding;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;
import jakarta.inject.Inject;

@AndroidEntryPoint
public class CompleteGamesFragment extends Fragment {

  @Inject
  CompleteGameAdapter adapter;

  private FragmentCompleteGamesBinding binding;
  private GameViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentCompleteGamesBinding.inflate(inflater, container, false);
    binding.codeLength.setOnSeekBarChangeListener((SeekBarOnlyChangeListener) (_, value, byUser) -> {
      binding.codeLengthValue.setText(String.valueOf(value));
      if (byUser) {
        viewModel.setCodeLength(value);
      }
    });
    binding.poolSize.setOnSeekBarChangeListener((SeekBarOnlyChangeListener) (_, value, byUser) -> {
      binding.poolSizeValue.setText(String.valueOf(value));
      if (byUser) {
        viewModel.setPoolSize(value);
      }
    });
    return binding.getRoot();
  }

  @SuppressLint("NotifyDataSetChanged")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    LifecycleOwner owner = getViewLifecycleOwner();
    binding.completeGames.setAdapter(adapter);
    viewModel
        .getCompleteGames()
        .observe(owner, (games) -> {
          adapter.clear();
          adapter.addAll(games);
          adapter.notifyDataSetChanged();
        });
    viewModel
        .getCodeLength()
        .observe(owner, binding.codeLength::setProgress);
    viewModel
        .getPoolSize()
        .observe(owner, binding.poolSize::setProgress);
  }

  @Override
  public void onDestroyView() {
    binding = null;
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
