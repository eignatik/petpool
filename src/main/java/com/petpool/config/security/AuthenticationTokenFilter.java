package com.petpool.config.security;

import com.petpool.application.exception.TokenAuthException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

  public AuthenticationTokenFilter(final RequestMatcher requiresAuth) {
    super(requiresAuth);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse)
      throws AuthenticationException, IOException, ServletException {
    //Authorization: Bearer TOKEN
    UsernamePasswordAuthenticationToken requestAuthentication = Optional
        .ofNullable(httpServletRequest.getHeader("Authorization"))
        .map(String::valueOf)
        .map(t -> StringUtils.removeStart(t, "Bearer").trim())
        .map(t -> new UsernamePasswordAuthenticationToken(t, t))
        .orElseThrow(() -> new TokenAuthException("You must send token in Authorization header."));

    return getAuthenticationManager().authenticate(requestAuthentication);

  }

  @Override
  protected void successfulAuthentication(final HttpServletRequest request,
      final HttpServletResponse response, final FilterChain chain, final Authentication authResult)
      throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    chain.doFilter(request, response);
  }


}


