package com.petpool.interfaces.auth.facade;

import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
  public User findById(Long id) {
    return userService.findById(id);
  }

}
