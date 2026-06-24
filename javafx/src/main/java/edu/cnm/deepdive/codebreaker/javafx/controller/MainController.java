package edu.cnm.deepdive.codebreaker.javafx.controller;

import edu.cnm.deepdive.codebreaker.model.Guess;
import edu.cnm.deepdive.codebreaker.service.CodebreakerService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {

  private static final String PROPS_FILE = "properties/code.properties";
  private static final String POOL_KEY = "pool";
  private static final String LENGTH_KEY = "length";

  @FXML
  private Button settings;
  @FXML
  private ListView<Guess> guesses;
  @FXML
  private Button submitGuess;
  @FXML
  private TextField guessInput;
  @FXML
  private Button startGame;

  @FXML
  private ResourceBundle resources;

  private String pool;
  private int length;

  public void shutdown() {
//    viewModel.shutdown();
  }

  @FXML
  void initialize() throws IOException {
    CodebreakerService service = CodebreakerService.getInstance();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPS_FILE)) {
      Properties properties = new Properties();
      properties.load(input);
      pool = properties.getProperty(POOL_KEY);
      length = Integer.parseInt(properties.getProperty(LENGTH_KEY));
    }
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
