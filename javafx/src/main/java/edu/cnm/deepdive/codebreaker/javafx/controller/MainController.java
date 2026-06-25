package edu.cnm.deepdive.codebreaker.javafx.controller;

import edu.cnm.deepdive.codebreaker.javafx.viewmodel.CodebreakerViewModel;
import edu.cnm.deepdive.codebreaker.model.Guess;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;

public class MainController {

  private static final String PROPS_FILE = "properties/code.properties";
  private static final String POOL_KEY = "pool";
  private static final String LENGTH_KEY = "length";

  @FXML
  private VBox main;
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
  private CodebreakerViewModel viewModel;

  public void shutdown() {
//    viewModel.shutdown();
  }

  @FXML
  void initialize() throws IOException {
    readGameProperties();
    setupViewModel();
    attachListeners();

    // TODO Pass a CellFactory to the guesses listview.
    // TODO Register an observer (consumer) of Game with the viewmodel:
    //    If game is not null:
    //      1. Clear out the observableList of guesses that the listview is holding.
    //      2. Add all of the guesses in the updated game to the listview's observable list.
    // TODO Register an observer of Throwable (error) with the viewmodel:
    //    If error is not null:
    //      1. Display a message to the user, indicating the error. (Probably a network connection/resolution/timeout error.)
  }

  private void attachListeners() {
    Consumer<Boolean> submissionController = (enabled) -> submitGuess.setDisable(!enabled);
    guessInput.setTextFormatter(
        new TextFormatter<>(new GuessInputFilter(pool, length, submissionController)));
  }

  private void readGameProperties() throws IOException {
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPS_FILE)) {
      Properties properties = new Properties();
      properties.load(input);
      pool = properties.getProperty(POOL_KEY);
      length = Integer.parseInt(properties.getProperty(LENGTH_KEY));
    }
  }

  private void setupViewModel() {
    viewModel = new CodebreakerViewModel();
    viewModel.observeGame((game) -> {
      // TODO: 6/25/26 Update UI with information from game.
      Popup popup = new Popup();
      VBox box = new VBox(10);
      box.setStyle("-fx-background-color: lightgray; -fx-padding: 20; -fx-border-color: black;");
      Text text = new Text();
      String message = game.guesses().isEmpty()
          ? "Game started successfully: " + game
          : "Guess submitted successfully: " + game.guesses().getLast();
      text.setText(message);
      box.getChildren().add(text);
      popup.getContent().add(box);
      popup.setAutoHide(true);
      popup.show(main, 50, 50);
    });
    viewModel.observeError((error) -> {
      // TODO: 6/25/26 Update UI with information from error.
    });
  }

  @FXML
  void startGame(ActionEvent actionEvent) {
    // TODO: 6/23/26 Use the viewmodel to start the game.
     viewModel.startGame("ABCDEF", 3);
  }

  @FXML
  void submitGuess(ActionEvent actionEvent) {
    // TODO: 6/23/26 Submit guess for current game.
    viewModel.submitGuess(guessInput.getText());
  }

  @FXML
  void showSettings(ActionEvent actionEvent) {
    // TODO: 6/23/26 Open settings window.
  }

}
