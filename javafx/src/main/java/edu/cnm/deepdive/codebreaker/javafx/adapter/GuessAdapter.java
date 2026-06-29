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
    private Label guessNumber;
    @FXML
    private Label guessText;
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
        guessNumber.setText(String.valueOf(position + 1));
        guessText.setText(item.text());
        exactMatches.setText(String.valueOf(item.exactMatches()));
        nearMatches.setText(String.valueOf(item.nearMatches()));
        // TODO: 6/29/26 Set additional style properties (background color, etc.)
        setGraphic(root);
      }
    }

  }


}
