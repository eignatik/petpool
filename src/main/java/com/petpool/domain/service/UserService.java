package com.petpool.domain.service;

import com.petpool.domain.model.user.User;

public interface UserService {

  User createUser(User user);

  User findById(Long id);

  User findByLogin(String login);

  User findByEmail(String email);

}
