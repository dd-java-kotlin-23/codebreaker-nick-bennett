package edu.cnm.deepdive.codebreaker.exception;

public class InvalidGuessException extends IllegalArgumentException {

  public InvalidGuessException() {
  }

  public InvalidGuessException(String message) {
    super(message);
  }

  public InvalidGuessException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidGuessException(Throwable cause) {
    super(cause);
  }

}
