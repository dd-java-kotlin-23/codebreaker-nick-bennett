package edu.cnm.deepdive.codebreaker.javafx.adapter;

import edu.cnm.deepdive.codebreaker.model.Guess;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class GuessAdapter implements Callback<ListView<Guess>, ListCell<Guess>> {

  @Override
  public ListCell<Guess> call(ListView<Guess> guessListView) {
    try {
      return new GuessCell(guessListView);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private class GuessCell extends ListCell<Guess> {

    private final ListView<Guess> listView;
    private final Parent root;

    @FXML
    private Label exactMatches;
    @FXML
    private Label nearMatches;

    private GuessCell(ListView<Guess> listView) throws IOException {
      this.listView = listView;
      FXMLLoader loader =
          new FXMLLoader(getClass().getClassLoader().getResource("layouts/guess.fxml"));
      loader.setController(this);
      root = loader.load();
      setText(null);
      setGraphic(null);
    }

    @Override
    protected void updateItem(Guess item, boolean empty) {
      super.updateItem(item, empty);
      if (item == null || empty) {
        setGraphic(null);
      } else {
        int position = listView.getItems().indexOf(item);
        exactMatches.setText(String.valueOf(item.exactMatches()));
        nearMatches.setText(String.valueOf(item.nearMatches()));
        setGraphic(root);
      }
    }

  }


}
