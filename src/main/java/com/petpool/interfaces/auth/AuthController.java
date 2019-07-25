package com.petpool.interfaces.auth;

import com.google.common.collect.ImmutableMap;
import com.petpool.domain.model.user.User;
import com.petpool.interfaces.auth.facade.AuthFacade;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private PasswordEncoder passwordEncoder;

  @Autowired
  public AuthController(AuthFacade authFacade) {
    this.authFacade = authFacade;
  }

  @Autowired
  public void setPasswordEncoder(
      PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
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
    passwordEncoder.encode("sdf");
    return authFacade
        .requestTokenForUser(username, password)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(405).build());
  }


}
