package com.petpool.config.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 *  This AuthenticationProvider used by spring security pipeline with AuthorizedUserAuthentication class.
 */
@Component
public class AuthenticationTokenProvider implements AuthenticationProvider {


  @Override
  public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

    if (authentication instanceof AuthorizedUserAuthentication) {

      return authentication;
    } else {
      throw new BadCredentialsException("Bad credentials");
    }
  }

  /**
   * Check if authentication provider can use this authentication data container(AuthorizedUserAuthentication).
   *
   * AuthorizedUserAuthentication  provided by AuthenticationTokenFilter
   * @see AuthenticationTokenFilter
   * @param authentication  authentication data container
   * @return true if we use correct authentication data container for this  authentication provider
   */
  @Override
  public boolean supports(final Class<?> authentication) {
    return AuthorizedUserAuthentication.class.equals(authentication);
  }
}
