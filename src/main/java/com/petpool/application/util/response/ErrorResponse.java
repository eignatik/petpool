package com.petpool.application.util.response;

public class ErrorResponse extends Response {

  public ErrorResponse(Error error) {
    super("", error);
  }
}
