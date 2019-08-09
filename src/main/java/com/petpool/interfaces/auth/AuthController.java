package com.petpool.interfaces.auth;

import com.google.common.collect.ImmutableMap;
import com.petpool.application.util.response.ErrorType;
import com.petpool.application.util.response.Response;
import com.petpool.interfaces.auth.facade.AuthFacade;
import com.petpool.interfaces.auth.facade.AuthFacade.Credentials;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
       @RequestHeader HttpHeaders headers) {

    return Optional.of(credentials)
        .filter(Credentials::isValid)
        .flatMap(cr -> authFacade.requestTokenForUser(cr, headers))
        .map(Response::ok)
        .orElse(Response.error(ErrorType.ACCESS_DENIED,"Invalid credentials"));
  }

  @PostMapping("refresh")
  public @ResponseBody
  ResponseEntity<Response> refreshToken(
      @RequestParam("refresh") final String refreshToken,
      @RequestHeader HttpHeaders headers) {

    return authFacade
        .refreshTokenForUser(refreshToken, headers)
        .map(Response::ok)
        .orElse(Response.error(ErrorType.BAD_REQUEST,"Refresh token incorrect"));
  }

  @GetMapping("check/unique")
  public @ResponseBody
  ResponseEntity<Response> checkUnique(
          @RequestParam(value="login", required=false) String login,
          @RequestParam(value="email", required=false) String email) {
    if(isNullOrEmpty(login) && isNullOrEmpty(email))
      return Response.error(ErrorType.BAD_REQUEST,"Request must have parameters");

    var isUniqueByUserName = authFacade.isUniqueByUserName(login.trim());
    var isUniqueByEmail = authFacade.isUniqueByEmail(email.trim());
    return Response.ok(ImmutableMap.of(
            "uniqueUserName", isUniqueByUserName,
            "uniqueEmail", isUniqueByEmail));
  }

  private boolean isNullOrEmpty(String str) {
    if(str != null && !str.trim().isEmpty())
      return false;
    return true;
  }
}
