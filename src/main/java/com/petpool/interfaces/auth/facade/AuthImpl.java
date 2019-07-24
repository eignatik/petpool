package com.petpool.interfaces.auth.facade;

import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthImpl implements AuthFacade {

  private final UserService userService;

  @Autowired
  public AuthImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public User createNewUser(User user) {
    return userService.createUser(user);
  }

  @Override
  public Optional<User> findById(Long id) {
    return userService.findById(id);
  }

  @Override
  public Optional<Map<String, String>> requestTokenForUser(String name, String password) {
    return userService
        .findByNameAndPassword(name, password)
        .map(this::generateToken);
  }

  private Map<String, String> generateToken(User user) {
    Map<String, String> map = new HashMap<>();
    map.put("accessToken", "");
    map.put("refreshToken", "");
    return map;
  }
}
