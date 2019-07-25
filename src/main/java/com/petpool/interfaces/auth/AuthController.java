package com.petpool.interfaces.auth;

import com.petpool.interfaces.auth.facade.AuthFacade;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
  ResponseEntity<Map<String, String>> getToken(
      @RequestParam("username") final String username,
      @RequestParam("password") final String password,
      @RequestHeader("User-Agent") String userAgent ) {
    return authFacade
        .requestTokenForUser(username, password, userAgent)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(405).build());
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
