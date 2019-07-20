package com.petpool.interfaces.authorization;

import com.google.common.collect.ImmutableMap;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import com.petpool.domain.model.user.UserTypes;
import com.petpool.interfaces.authorization.facade.AuthorizationFacade;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

  private AuthorizationFacade authorizationFacade;

  @Autowired
  public AuthorizationController(AuthorizationFacade authorizationFacade) {
    this.authorizationFacade = authorizationFacade;
  }

  @Transactional
  @GetMapping("/create/user")
  public Map<String, Object> createUser() {
    User user = new User(
        "",
        "",
        "",
        new Date(),
        Set.of(new UserType(UserTypes.ADMIN)));

    User newUser = authorizationFacade.createNewUser(user);

    return ImmutableMap.of("userId", newUser != null ? newUser.getId() : "0");
  }

  @Transactional
  @GetMapping("/get/user")
  public Map<String, Object> getUser() {
    User newUser = authorizationFacade.findById(7L);

    return ImmutableMap.of("userId", newUser != null ? newUser.getId() : "0");
  }

}
