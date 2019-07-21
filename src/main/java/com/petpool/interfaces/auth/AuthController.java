package com.petpool.interfaces.auth;

import com.google.common.collect.ImmutableMap;
import com.petpool.interfaces.auth.facade.AuthFacade;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private AuthFacade authFacade;

  @Autowired
  public AuthController(AuthFacade authFacade) {
    this.authFacade = authFacade;
  }

  @PostMapping("create/user")
  public @ResponseBody Map<String, Object> createUser(@RequestBody Map<String, Object> userInfo) {

    return ImmutableMap.of("", "");
  }

}
