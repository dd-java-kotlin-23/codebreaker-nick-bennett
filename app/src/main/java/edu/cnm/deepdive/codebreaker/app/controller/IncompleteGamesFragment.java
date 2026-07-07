package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentIncompleteGamesBinding;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;

public class IncompleteGamesFragment extends Fragment {

  private FragmentIncompleteGamesBinding binding;
  private GameViewModel viewModel;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    binding = FragmentIncompleteGamesBinding.inflate(inflater, container, false);
    // TODO: 7/7/26 Attach listeners, etc.
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    // TODO: 7/7/26 Observe livedata containing incomplete games.
    viewModel
        .getIncompleteGames()
        .observe(getViewLifecycleOwner(), (games) -> {
          // TODO: 7/7/26 Pass games to recyclerview adapter.
        });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}