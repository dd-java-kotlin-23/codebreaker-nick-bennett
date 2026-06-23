package edu.cnm.deepdive.codebreaker.javafx;

import edu.cnm.deepdive.codebreaker.javafx.controller.MainController;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  private static final String BUNDLE_BASE_NAME = "bundles/ui-strings";
  private static final String LAYOUT_NAME = "layouts/main.fxml";
  private static final String WINDOW_TITLE_KEY = "windowTitle";

  private MainController controller;

  static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME);
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(LAYOUT_NAME), bundle);
    Parent root = loader.load();
    controller = loader.getController();
    Scene scene = new Scene(root);
    stage.setTitle(bundle.getString(WINDOW_TITLE_KEY));
    stage.setResizable(false); // TODO: 6/23/26 Investigate resizing.
    // TODO: 6/23/26 Set additional properties (e.g., icon) of stage.
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    // TODO: 6/23/26 Invoke methods on controller (etc.) as necessary for shutting down.
  }

}
