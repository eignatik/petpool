package com.petpool.config.security;

import com.petpool.application.exception.TokenAuthException;
import com.petpool.application.util.response.ErrorType;
import com.petpool.config.security.JwtCodec.ExpirationTokenException;
import com.petpool.config.security.JwtCodec.InvalidPayloadTokenException;
import com.petpool.config.security.JwtCodec.InvalidSignatureTokenException;
import com.petpool.config.security.JwtCodec.Payload;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Class extract access token from request header(Authorization: Bearer
 * eyJhbGciOiJIUzI1NiIXVCJ9...TJVA95OrM7E20RMHrHDcEfxjoYZgeFONFh7HgQ) and pass it to
 * AuthenticationTokenProvider
 *
 * @see AuthenticationTokenProvider
 */
public class AuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

  /**
   * Use for getting error type in RestAuthenticationEntryPoint
   *
   * @see RestAuthenticationEntryPoint
   */
  public static final String ERROR_TOKEN_AUTH_DETAIL_ATTRIBUTE = "error_token_auth_detail";
  public static final String ERROR_TOKEN_AUTH_DETAIL_MSG_ATTRIBUTE = "error_token_auth_detail_msg";
  private final JwtCodec jwtCodec;

  public AuthenticationTokenFilter(final RequestMatcher requiresAuth, JwtCodec jwtCodec) {
    super(requiresAuth);
    this.jwtCodec = jwtCodec;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse)
      throws AuthenticationException, IOException, ServletException {
    //Authorization: Bearer TOKEN
    String token = Optional
        .ofNullable(httpServletRequest.getHeader("Authorization"))
        .map(String::valueOf)
        .map(t -> StringUtils.removeStart(t, "Bearer").trim())
        .orElseThrow(() -> {
          httpServletRequest.setAttribute(ERROR_TOKEN_AUTH_DETAIL_ATTRIBUTE, ErrorType.BAD_TOKEN);
          httpServletRequest.setAttribute(ERROR_TOKEN_AUTH_DETAIL_MSG_ATTRIBUTE,
              "You must send token in Authorization header.");
          return new TokenAuthException("You must send token in Authorization header.");
        });

    Authentication requestAuthentication = parseToken(token, httpServletRequest);

    return getAuthenticationManager().authenticate(requestAuthentication);

  }

  private Authentication parseToken(String token, HttpServletRequest httpServletRequest)
      throws TokenAuthException {
    Payload payload;
    ErrorType errorType = null;
    String msg = "";
    try {
      payload = jwtCodec.parseToken(token);
    } catch (InvalidPayloadTokenException e) {
      errorType = ErrorType.BAD_TOKEN;
      msg = "Invalid token's payload";
      throw new TokenAuthException(msg, e);

    } catch (InvalidSignatureTokenException e) {
      errorType = ErrorType.BAD_TOKEN;
      msg = "Invalid token's signature";
      throw new TokenAuthException(msg, e);

    } catch (ExpirationTokenException e) {
      errorType = ErrorType.TOKEN_EXPIRED;
      msg = "Token is expired";
      throw new TokenAuthException(msg, e);

    } catch (Exception e) {
      errorType = ErrorType.OTHER_AUTH_ERROR;
      msg = "Other token's parser exception";
      throw new TokenAuthException(msg, e);

    } finally {
      if (errorType != null) {
        httpServletRequest.setAttribute(ERROR_TOKEN_AUTH_DETAIL_MSG_ATTRIBUTE, msg);
        httpServletRequest.setAttribute(ERROR_TOKEN_AUTH_DETAIL_ATTRIBUTE, errorType);
      }
    }

    return new AuthorizedUserAuthentication(
        new AuthorizedUser(payload.getUserId(), payload.getRoles()));
  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain chain, final Authentication authResult)
      throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    chain.doFilter(request, response);
  }


}


