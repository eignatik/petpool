package com.petpool.application.util.response;

public class ErrorResponse extends Response<String> {

  public ErrorResponse(Error error) {
    super("", error);
  }
}
