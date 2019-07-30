package com.petpool.interfaces.auth;

import com.petpool.application.util.response.ErrorType;
import com.petpool.application.util.response.Response;
import com.petpool.interfaces.auth.facade.AuthFacade;
import com.petpool.interfaces.auth.facade.AuthFacade.Credentials;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class AuthController {

  private AuthFacade authFacade;

  @Autowired
  public AuthController(AuthFacade authFacade) {
    this.authFacade = authFacade;
  }


  @PostMapping("request")
  public @ResponseBody
  ResponseEntity<Response> getToken(@RequestBody Credentials credentials,
      @RequestHeader("User-Agent") String userAgent) {

    return Optional.of(credentials)
        .filter(Credentials::isValid)
        .flatMap(cr -> authFacade.requestTokenForUser(cr, userAgent))
        .map(Response::ok)
        .orElse(Response.error(ErrorType.ACCESS_DENIED,"Invalid credentials"));
  }

  @PostMapping("refresh")
  public @ResponseBody
  ResponseEntity<Response> refreshToken(
      @RequestParam("refresh") final String refreshToken,
      @RequestHeader("User-Agent") String userAgent) {
    return authFacade
        .refreshTokenForUser(refreshToken, userAgent)
        .map(Response::ok)
        .orElse(Response.error(ErrorType.BAD_REQUEST,""));
  }
}
