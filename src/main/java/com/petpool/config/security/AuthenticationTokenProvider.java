package com.petpool.config.security;

import com.petpool.application.exception.TokenAuthException;
import com.petpool.application.exception.UserNotFoundException;
import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Uses token for get user from database and token's verification.
 */
@Component
public class AuthenticationTokenProvider extends AbstractUserDetailsAuthenticationProvider {

  private final UserService userService;

  @Autowired
  public AuthenticationTokenProvider(UserService userService) {
    this.userService = userService;
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {
    //not required
  }

  /**
   *
   *
   * <p>The function retrieveUser return correct authorized user in specific format UserDetails</p>
   *
   * @see UserDetails
   *
   * @param userName
   * @param usernamePasswordAuthenticationToken
   * @return
   * @throws AuthenticationException
   */
  @Override
  protected UserDetails retrieveUser(String userName,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {

    String tokenStr = String.valueOf(usernamePasswordAuthenticationToken.getCredentials());
    if (!tokenVerefication(tokenStr)) {
      throw new TokenAuthException("Token is invalid!");
    }
    TokenRepresentation token = parseToken(tokenStr);
    if (!checkTokenExpiration(token)) {
      throw new TokenAuthException("Token expired!");
    }

    User user = userService.findById(token.userID)
        .orElseThrow(
            () -> new UserNotFoundException("User with id =" + token.userID + " not found."));

    return getUserDetails(user);

  }

  private UserDetails getUserDetails(User user) {
    Set<Role> roles = user.getRoles();
    return new org.springframework.security.core.userdetails.User(
        user.getUserName(),
        user.getPasswordHash(),
        true, true, true, true,
        roles.stream().map(r -> r.getUserType().getValue()).map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList())
    );

  }


  private TokenRepresentation parseToken(String token) {
    return new TokenRepresentation(1, "qweqwe", new Date(System.currentTimeMillis()));
  }

  private boolean tokenVerefication(String token) {
    return true;
  }

  private boolean checkTokenExpiration(TokenRepresentation token) {
    //проверим даты из токера и текущей
    return true;
  }

  @Data
  @AllArgsConstructor
  private static class TokenRepresentation {

    private long userID;
    private String userName;
    private Date expired;
  }

}
