package edu.cnm.deepdive.codebreaker.model;

import java.time.OffsetDateTime;

// TODO: 6/15/26 Add guesses parameter after defining the Guess record.
public record Game(String id, String pool, int length, OffsetDateTime created) {

}
