package com.petpool.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * User for get authorized user information
 */
@Component
public class AuthContext {

  public  AuthorizedUser getUser(){
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (principal==null) return AuthorizedUser.GUEST;
    if (principal instanceof AuthorizedUser) {
     return (AuthorizedUser) principal;
    } else return AuthorizedUser.GUEST;
  }

}
