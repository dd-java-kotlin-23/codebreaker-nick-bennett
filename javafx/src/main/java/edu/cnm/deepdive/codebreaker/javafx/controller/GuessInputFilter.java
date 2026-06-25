package edu.cnm.deepdive.codebreaker.javafx.controller;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

class GuessInputFilter implements UnaryOperator<TextFormatter.Change> {

  private static final String PATTERN_FORMAT= "[^%s]+";

  private final String pool; // Assumed to be uppercase.
  private final int length;
  private final Consumer<Boolean> listener;
  private final Pattern pattern;

  GuessInputFilter(String pool, int length, Consumer<Boolean> listener) {
    this.pool = pool;
    this.length = length;
    pattern = Pattern.compile(PATTERN_FORMAT.formatted(pool));
    this.listener = listener;
  }

  @Override
  public Change apply(Change change) {
    if (!change.isDeleted()) {
      String previousText = change.getControlText();
      int prefixLength = change.getRangeStart();
      int suffixStart = change.getRangeEnd();
      int previousLength = previousText.length();
      int suffixLength = previousLength - suffixStart;
      String newText = pattern
          .matcher(change.getText().toUpperCase())
          .replaceAll("");
      if (newText.isEmpty() || prefixLength + newText.length() + suffixLength <= length) {
        change.setText(newText);
      } else {
        StringBuilder builder =
            new StringBuilder(newText).append(previousText, suffixStart, previousLength);
        change.setRange(prefixLength, previousLength);
        change.setText(builder.substring(0, length - prefixLength));
      }
    }
    listener.accept(change.getControlNewText().length() == length);
    return change;
  }

}
