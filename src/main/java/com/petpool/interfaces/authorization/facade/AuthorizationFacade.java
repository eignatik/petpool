package com.petpool.interfaces.authorization.facade;

import com.petpool.domain.model.user.User;

public interface AuthorizationFacade {

  User createNewUser(User user);

  User findById(Long id);

}
