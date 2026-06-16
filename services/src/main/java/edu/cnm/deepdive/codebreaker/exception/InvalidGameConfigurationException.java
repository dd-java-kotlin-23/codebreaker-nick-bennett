package edu.cnm.deepdive.codebreaker.exception;

public class InvalidGameConfigurationException extends IllegalArgumentException {

  public InvalidGameConfigurationException() {
  }

  public InvalidGameConfigurationException(String message) {
    super(message);
  }

  public InvalidGameConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidGameConfigurationException(Throwable cause) {
    super(cause);
  }

}
