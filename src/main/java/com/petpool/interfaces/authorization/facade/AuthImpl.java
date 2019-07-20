package com.petpool.interfaces.authorization.facade;

import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class AuthImpl implements AuthorizationFacade {

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
  public User findById(Long id) {
    return userService.findById(id);
  }
}
