package edu.cnm.deepdive.codebreaker.javafx.controller;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

class GuessInputFilter implements UnaryOperator<TextFormatter.Change> {

  private static final String PATTERN_FORMAT= "[^%s]+";

  private final String pool; // Assumed to be uppercase.
  private final int length;
  private final Pattern pattern;

  GuessInputFilter(String pool, int length) {
    this.pool = pool;
    this.length = length;
    pattern = Pattern.compile(PATTERN_FORMAT.formatted(pool));
  }

  @Override
  public Change apply(Change change) {
    if (!change.isDeleted()) {
      String newText = change.getText().toUpperCase();
      newText = pattern.matcher(newText).replaceAll("");
      change.setText(newText);
      int newLength = change.getRangeStart() + newText.length();
      if (newLength > length) {
        int substringLength = newLength - length;
        newText = newText.substring(0, substringLength);
        change.setRange(change.getRangeStart(), change.getControlText().length());
        change.setText(newText);
      } else {
        newLength = newLength + change.getControlText().length() - change.getRangeStart();
        if (newLength > length) {
          // TODO: 6/24/26 Calculate how many more characters need to be deleted.
          int excess = newLength - length - (change.getRangeEnd() - change.getRangeStart());
          if (excess > 0) {

          }
        }
      }
      if (change.isAdded()) {

      } else  {

      }
    }
    return change;
  }

}
