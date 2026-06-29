package edu.cnm.deepdive.codebreaker.javafx.controller;

import edu.cnm.deepdive.codebreaker.javafx.adapter.GuessAdapter;
import edu.cnm.deepdive.codebreaker.javafx.viewmodel.CodebreakerViewModel;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;

public class MainController implements Stoppable {

  private static final String PROPS_FILE = "properties/code.properties";
  private static final String POOL_KEY = "pool";
  private static final String LENGTH_KEY = "length";

  private final CodebreakerViewModel viewModel;

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
  private boolean inProgress;

  @Inject
  public MainController(CodebreakerViewModel viewModel) {
    this.viewModel = viewModel;
  }

  @Override
  public void shutdown() {
    viewModel.shutdown();
  }

  @FXML
  void initialize() throws IOException {
    readGameProperties();
    setupViewModel();
    attachListeners();
    updateGuessControls(null);
  }

  private void attachListeners() {
    Consumer<Boolean> submissionController = (enabled) -> submitGuess.setDisable(!inProgress || !enabled);
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
    viewModel.observeGame(this::handleGame);
    viewModel.observeError((error) -> {
      // TODO: 6/25/26 Update UI with information from error.
    });
  }

  private void handleGame(Game game) {
    // TODO: 6/25/26 Update UI with information from game.
    guesses.setCellFactory(new GuessAdapter());
    guesses.getItems().clear();
    guesses.getItems().addAll(game.guesses());
    Platform.runLater(() -> guesses.scrollTo(game.guesses().size() - 1));
    inProgress = !game.isSolved();
    updateGuessControls(game);
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

  private void updateGuessControls(Game game) {
    boolean enabled = (game != null && !game.isSolved());
    guessInput.requestFocus();
    guessInput.setDisable(!enabled);
    submitGuess.setDisable(!enabled);
  }

}
