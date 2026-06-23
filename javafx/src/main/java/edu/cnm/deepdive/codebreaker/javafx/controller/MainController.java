package edu.cnm.deepdive.codebreaker.javafx.controller;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {

  @FXML
  private Button settings;

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
  void initialize() {
    submitGuess.setDisable(true);
    // TODO Pass a CellFactory to the guesses listview.
    // TODO Register an observer (consumer) of Game with the viewmodel:
    //    If game is not null:
    //      1. Clear out the observableList of guesses that the listview is holding.
    //      2. Add all of the guesses in the updated game to the listview's observable list.
    // TODO Register an observer of Throwable (error) with the viewmodel:
    //    If error is not null:
    //      1. Display a message to the user, indicating the error. (Probably a network connection/resolution/timeout error.)
  }

  @FXML
  void startGame(ActionEvent actionEvent) {
    // TODO: 6/23/26 Use the viewmodel to start the game.
    // viewModel.startGame("ABCDEF", 3);
  }

  @FXML
  void submitGuess(ActionEvent actionEvent) {
    // TODO: 6/23/26 Submit guess for current game.
    // viewModel.submitGuess(...)
  }

  @FXML
  void showSettings(ActionEvent actionEvent) {
    // TODO: 6/23/26 Open settings window.
  }

}
