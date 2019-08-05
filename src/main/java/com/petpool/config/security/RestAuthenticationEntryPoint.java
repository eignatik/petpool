package com.petpool.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petpool.application.util.response.ErrorType;
import com.petpool.application.util.response.Response;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Response error state by authorization process.
 *
 * Error state present in ErrorType
 * @see ErrorType
 */
@Component
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException authException) throws IOException {

    Object errorTypeAttribute = request
        .getAttribute(AuthenticationTokenFilter.ERROR_TOKEN_AUTH_DETAIL_ATTRIBUTE);
    Object msgAttribute = request
        .getAttribute(AuthenticationTokenFilter.ERROR_TOKEN_AUTH_DETAIL_MSG_ATTRIBUTE);

    ErrorType errorType = ErrorType.OTHER_AUTH_ERROR;
    String msg = "Need authentication";

    if (errorTypeAttribute != null) {
      errorType = (ErrorType) errorTypeAttribute;
    }

    if (msgAttribute != null) {
      msg = (String) msgAttribute;
    }

    ObjectMapper mapper = new ObjectMapper();
    response.getWriter().write(mapper.writeValueAsString(Response.error(errorType, msg)));
    response.setStatus(200);
    response.setContentType("application/json;charset=UTF-8");
  }

}

