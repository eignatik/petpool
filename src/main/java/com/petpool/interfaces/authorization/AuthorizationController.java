package com.petpool.interfaces.authorization;

import com.google.common.collect.ImmutableMap;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserRepository;
import com.petpool.domain.model.user.UserType;
import com.petpool.domain.model.user.UserTypes;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationController {

  private UserRepository repository;

  public AuthorizationController(UserRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/get/user")
  public Map<String, Object> getUser() {
    User user = new User("", "", "", new Date(), new UserType(UserTypes.ADMIN));
    repository.save(user);
    Optional<User> byId = repository.findById(1L);
    return ImmutableMap.of("userId", byId.isPresent() ? byId.get() : "0");
  }

}
