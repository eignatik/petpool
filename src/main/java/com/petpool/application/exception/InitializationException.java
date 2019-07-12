package com.petpool.application.exception;

/**
 * General exception to be thrown in case of issues with initializing of the application.
 */
public class InitializationException extends RuntimeException {

  public InitializationException(String message) {
    super(message);
  }

  public InitializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
