package com.petpool.application.exception;

import com.petpool.application.util.response.ErrorType;
import org.springframework.security.core.AuthenticationException;

/**
 * Present token authentication error. Used by spring security.
 */
public class TokenAuthException extends AuthenticationException {

  private  ErrorType type;

  public TokenAuthException(String msg, ErrorType type, Throwable t) {
    super(msg, t);
    this.type = type;
  }

  public TokenAuthException(String msg) {
    super(msg);
  }

  public ErrorType getType() {
    return type;
  }
}
