package com.petpool.interfaces.auth.facade;

import com.petpool.domain.model.user.User;

public interface AuthFacade {

  User createNewUser(User user);

  User findById(Long id);

}
