package com.petpool.application.impl;

import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserRepository;
import com.petpool.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User findById(Long id) {
    return userRepository.findById(id).orElse(null);
  }

  @Override
  public User findByLogin(String login) {
    return null;
  }

  @Override
  public User findByEmail(String email) {
    return null;
  }
}
