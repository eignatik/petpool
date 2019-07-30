package com.petpool.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petpool.application.exception.TokenAuthException;
import com.petpool.application.util.response.Error;
import com.petpool.application.util.response.ErrorType;
import com.petpool.application.util.response.Response;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException authException) throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    response.getWriter().write(mapper.writeValueAsString(Response.error(ErrorType.BAD_TOKEN,"Token is expired or invalid.")));
    response.setStatus(200);
    response.setContentType("application/json;charset=UTF-8");
  }

}

