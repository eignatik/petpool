package com.petpool.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petpool.application.util.response.ErrorType;
import com.petpool.application.util.response.Response;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final AccessDeniedException ex) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(Response.error(ErrorType.ACCESS_DENIED,"Access denied!")));
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");

    }

}
