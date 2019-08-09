package com.petpool.interfaces.auth.facade;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;

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
   * @param headers   http headers
   * @return         with generated tokens.
   */
  Optional<GeneratedToken> requestTokenForUser(Credentials credentials, HttpHeaders headers);

  /**
   * Creates new token instead of a given refresh token if it's valid and trusted.
   *
   * <p>It needed in case if there is a need to refresh a token as it's expired.</p>
   *
   * @param refreshToken  token to be refreshed
   * @param headers     user agent
   * @return               new refreshed token
   */
  Optional<GeneratedToken> refreshTokenForUser(String refreshToken, HttpHeaders headers);

  Boolean isUniqueByUserName(String login);

  Boolean isUniqueByEmail(String email);

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
