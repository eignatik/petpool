package com.petpool.interfaces.user.facade;

import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserFacadeImpl implements UserFacade {

  private final UserService userService;

  @Autowired
  public UserFacadeImpl(UserService userService) {
    this.userService = userService;
  }



  @Override
  public Optional<User> findById(Long id) {
    return userService.findById(id);
  }

}
