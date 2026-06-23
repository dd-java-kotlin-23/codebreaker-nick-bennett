package edu.cnm.deepdive.codebreaker.javafx.controller;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {

  @FXML
  private ListView guesses; // FIXME: 6/23/26 Add the type parameter for Guess.

  @FXML
  private Button submitGuess;

  @FXML
  private TextField guessInput;

  @FXML
  private Button startGame;

  @FXML
  private ResourceBundle resources;

  public void shutdown() {
//    viewModel.shutdown();
  }

  @FXML
  void startGame(ActionEvent actionEvent) {
    // TODO: 6/23/26 Use the viewmodel to start the game.
    // viewModel.startGame("ABCDEF", 3);
  }

  @FXML
  void submitGuess(ActionEvent actionEvent) {
    // TODO: 6/23/26 Submit guess for current game.
  }

}
