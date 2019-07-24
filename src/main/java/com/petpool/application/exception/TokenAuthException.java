package com.petpool.application.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenAuthException extends AuthenticationException {

  public TokenAuthException(String msg, Throwable t) {
    super(msg, t);
  }

  public TokenAuthException(String msg) {
    super(msg);
  }
}
