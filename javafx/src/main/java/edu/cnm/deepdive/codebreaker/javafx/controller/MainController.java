package edu.cnm.deepdive.codebreaker.javafx.controller;

import edu.cnm.deepdive.codebreaker.javafx.viewmodel.CodebreakerViewModel;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

  @FXML
  private Button startGame;

  @FXML
  private ResourceBundle resources;

  private CodebreakerViewModel viewModel;

  public CodebreakerViewModel getViewModel() {
    return viewModel;
  }

  public void setViewModel(CodebreakerViewModel viewModel) {
    this.viewModel = viewModel;
  }

  public void shutdown() {
    viewModel.shutdown();
  }

  @FXML
  void startGame(ActionEvent actionEvent) {
    // TODO: 6/23/26 Use the viewmodel to start the game.
  }

}
