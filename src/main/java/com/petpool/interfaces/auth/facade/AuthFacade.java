package com.petpool.interfaces.auth.facade;

import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the business logic of the auth flow.
 *
 * <p>Provides with all common methods to describe business processes regarding auth related
 * cases. Used as a responsible part of components chain in the application to get indirect
 * access to application services.
 * Consider to be used only in the related controller.</p>
 */
public interface AuthFacade {

  /**
   * Requests new set of access and refresh tokens for given user.
   *
   * @see Credentials
   * @param credentials data object with user credentials
   * @param userAgent   user agent
   * @return map        with generated tokens.
   */
  Optional<Map<String, String>> requestTokenForUser(Credentials credentials, String userAgent);

  /**
   * Creates new token instead of a given refresh token if it's valid and trusted.
   *
   * <p>It needed in case if there is a need to refresh a token as it's expired.</p>
   *
   * @param refreshToken  token to be refreshed
   * @param userAgent     user agent
   * @return              map with new refreshed token
   */
  Optional<Map<String, String>> refreshTokenForUser(String refreshToken, String userAgent);

  @Data
  @AllArgsConstructor
  class Credentials {

    private String email;
    private String name;
    private String password;

    public Credentials() {
    }

    public boolean isValid() {
      return !(email.isEmpty() && name.isEmpty() || password.isEmpty());
    }

  }

}
