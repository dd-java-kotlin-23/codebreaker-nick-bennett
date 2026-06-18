package edu.cnm.deepdive.codebreaker.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Function;

// TODO: 6/15/26 Add guesses parameter after defining the Guess record.
public record Game(String id, String pool, int length, List<Guess> guesses,
                   OffsetDateTime created) {

  public boolean isSolved() {
    //noinspection Convert2MethodRef // TODO Explore use of method reference.
    return guesses
        .stream()
        .anyMatch((guess) -> guess.solution());
  }

  /**
   * Returns the secret code if it has already been guessed, and null otherwise.
   *
   * @return
   */
  public String getCode() {
    return guesses
        .stream()
        .filter(Guess::solution)
        .map(new Function<Guess, String>() {
          @Override
          public String apply(Guess guess) {
            return guess.text();
          }
        })
        .findFirst()
        .orElse(null);
  }

}
