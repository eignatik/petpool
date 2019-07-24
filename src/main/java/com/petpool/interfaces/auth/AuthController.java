package com.petpool.interfaces.auth;

import com.google.common.collect.ImmutableMap;
import com.petpool.domain.model.user.User;
import com.petpool.interfaces.auth.facade.AuthFacade;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PostMapping("create/user")
  public @ResponseBody
  Map<String, Object> createUser(@RequestBody Map<String, Object> userInfo) {

    return ImmutableMap.of("", "");
  }

  @PostMapping("request")
  public @ResponseBody
  ResponseEntity<Map<String, String>> getToken(@RequestParam("username") final String username,
      @RequestParam("password") final String password) {
    return authFacade
        .requestTokenForUser(username, password)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(405).build());
  }

}
