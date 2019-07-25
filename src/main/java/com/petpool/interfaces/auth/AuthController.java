package com.petpool.interfaces.auth;

import static java.util.Optional.empty;

import com.petpool.interfaces.auth.facade.AuthFacade;
import com.petpool.interfaces.auth.facade.AuthFacade.Credentials;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  ResponseEntity<Map<String, String>> getToken(@RequestBody Credentials credentials,
      @RequestHeader("User-Agent") String userAgent) {

    return Optional.of(credentials)
        .filter(Credentials::isValid)
        .flatMap(cr -> authFacade.requestTokenForUser(cr, userAgent))
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }

  @PostMapping("refresh")
  public @ResponseBody
  ResponseEntity<Map<String, String>> refreshToken(
      @RequestParam("refresh") final String refreshToken,
      @RequestHeader("User-Agent") String userAgent) {
    return authFacade
        .refreshTokenForUser(refreshToken, userAgent)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(405).build());
  }
}
