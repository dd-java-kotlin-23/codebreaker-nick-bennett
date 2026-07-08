package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.adapter.IncompleteGameAdapter;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentIncompleteGamesBinding;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;
import jakarta.inject.Inject;

@AndroidEntryPoint
public class IncompleteGamesFragment extends Fragment {

  private static final String TAG = IncompleteGamesFragment.class.getSimpleName();

  @Inject
  IncompleteGameAdapter adapter;

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
    adapter.setListener((v, game) -> Log.d(TAG, game.toString()));
    binding.incompleteGames.setAdapter(adapter);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    viewModel
        .getIncompleteGames()
        .observe(getViewLifecycleOwner(), (games) -> {
          adapter.clear();
          adapter.addAll(games);
          adapter.notifyDataSetChanged();
        });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

}