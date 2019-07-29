package com.petpool.config.security;

import com.petpool.application.exception.TokenAuthException;
import com.petpool.config.security.JwtCodec.ExpirationTokenException;
import com.petpool.config.security.JwtCodec.InvalidPayloadTokenException;
import com.petpool.config.security.JwtCodec.InvalidSignatureTokenException;
import com.petpool.config.security.JwtCodec.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Uses token for get user from database and token's verification.
 */
@Component
public class AuthenticationTokenProvider extends AbstractUserDetailsAuthenticationProvider {


  private JwtCodec jwtCodec;

  @Autowired
  public void setJwtCodec(JwtCodec jwtCodec) {
    this.jwtCodec = jwtCodec;
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {
    //not required
  }

  /**
   * <p>The function retrieveUser return correct authorized user in specific format UserDetails</p>
   *
   * @see UserDetails
   */
  @Override
  protected UserDetails retrieveUser(String userName,
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken)
      throws AuthenticationException {

    String tokenStr = String.valueOf(usernamePasswordAuthenticationToken.getCredentials());

    Payload payload;
    try {
      payload = jwtCodec.parseToken(tokenStr);
    } catch (InvalidPayloadTokenException e) {
      throw new TokenAuthException("Invalid token's payload", e);
    } catch (InvalidSignatureTokenException e) {
      throw new TokenAuthException("Invalid token's signature", e);
    } catch (ExpirationTokenException e) {
      throw new TokenAuthException("Token is expired", e);
    } catch (Exception e) {
      throw new TokenAuthException("Other token's parser exception",e);
    }
    return new AuthorizedUser(payload.getUserId(), payload.getRoles());

  }
}
