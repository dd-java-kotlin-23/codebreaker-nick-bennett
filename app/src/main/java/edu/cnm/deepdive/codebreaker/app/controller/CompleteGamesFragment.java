package edu.cnm.deepdive.codebreaker.app.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.codebreaker.app.viewmodel.GameViewModel;

@AndroidEntryPoint
public class CompleteGamesFragment extends Fragment {

  private GameViewModel viewModel;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // TODO: 7/9/26 Use View binding to inflate layout; attach listeners; return root view of the
    //  binding object.
    return super.onCreateView(inflater, container, savedInstanceState);
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

}
