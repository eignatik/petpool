package com.petpool.application.util.response;

public class SimpleError extends Error<String> {

  public SimpleError(ErrorType type, String message) {
    super(type, message, "");
  }
}
