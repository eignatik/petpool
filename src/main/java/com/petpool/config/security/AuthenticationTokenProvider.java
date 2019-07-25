package com.petpool.config.security;

import com.petpool.application.exception.TokenAuthException;
import com.petpool.application.exception.UserNotFoundException;
import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import java.util.Date;
import java.util.Optional;
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
    if (!tokenVerification(tokenStr)) {
      throw new TokenAuthException("Token is invalid!");
    }
    TokenRepresentation token = parseToken(tokenStr).orElseThrow(() -> new TokenAuthException("Token is not correct!"));
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


  private Optional<TokenRepresentation> parseToken(String token) {
    String[] split = token.split(":");
    if(split.length<3) return Optional.empty();
    long userID= Long.parseLong(split[0]);
    long expired = Long.parseLong(split[1]);

    return userService.findById(userID)
        .map(user-> new TokenRepresentation(user.getId(), user.getUserName(), new Date(expired)));

  }

  private boolean tokenVerification(String token) {
    return true;
  }

  private boolean checkTokenExpiration(TokenRepresentation token) {
    return token.getExpired().after(new Date(System.currentTimeMillis()));
  }

  @Data
  @AllArgsConstructor
  private static class TokenRepresentation {
    private long userID;
    private String userName;
    private Date expired;
  }

}
