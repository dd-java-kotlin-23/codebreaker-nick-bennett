package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.R;
import edu.cnm.deepdive.codebreaker.app.adapter.IncompleteGameAdapter;
import edu.cnm.deepdive.codebreaker.app.databinding.FragmentIncompleteGamesBinding;
import edu.cnm.deepdive.codebreaker.app.model.entity.IncompleteGame;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;
import edu.cnm.deepdive.codebreaker.model.Game;
import jakarta.inject.Inject;

@AndroidEntryPoint
public class IncompleteGamesFragment extends Fragment {

  private static final String TAG = IncompleteGamesFragment.class.getSimpleName();

  @Inject
  IncompleteGameAdapter adapter;

  private FragmentIncompleteGamesBinding binding;
  private GameViewModel viewModel;
  private Game game;

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
    adapter.setListener(this::displayActionMenu);
    binding.incompleteGames.setAdapter(adapter);
    viewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);
    viewModel
        .getIncompleteGames()
        .observe(getViewLifecycleOwner(), (games) -> {
          adapter.clear();
          adapter.addAll(games);
          adapter.notifyDataSetChanged();
        });
    viewModel
        .getGame()
        .observe(getViewLifecycleOwner(), (game) -> {
          if (this.game != null && !this.game.equals(game)) {
            showGameFragment();
          }
          this.game = game;
        });
  }

  @Override
  public void onDestroyView() {
    binding = null;
    super.onDestroyView();
  }

  private void displayActionMenu(View view, IncompleteGame game) {
    PopupMenu popup = new PopupMenu(requireContext(), view, Gravity.TOP | Gravity.END);
    Menu menu = popup.getMenu();
    popup.getMenuInflater().inflate(R.menu.incomplete_game_options, menu);
    MenuItem deleteItem = menu.findItem(R.id.delete_game);
    MenuItem playItem = menu.findItem(R.id.play_game);
    if (!game.getExternalKey().equals(this.game.id())) {
      deleteItem.setOnMenuItemClickListener((_) -> {
        viewModel.deleteGame(game.getExternalKey());
        return true;
      });
      playItem.setOnMenuItemClickListener((_) -> {
        viewModel.getGame(game.getExternalKey());
        return true;
      });
    } else {
      deleteItem.setVisible(false);
      playItem.setOnMenuItemClickListener((_) -> {
        showGameFragment();
        return true;
      });
    }
    popup.show();
  }

  private void showGameFragment() {
    NavController controller = Navigation.findNavController(binding.getRoot());
    controller.navigate(IncompleteGamesFragmentDirections.showGameFragment());
  }

}