package com.petpool.application.impl;

import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserRepository;
import com.petpool.domain.model.user.RoleRepository;
import com.petpool.domain.model.user.UserType;
import com.petpool.domain.service.UserService;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public Optional<User> findByLogin(String login) {
    return userRepository.findUserByUserName(login);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Override
  public Optional<Role> findRoleByType(UserType userType) {
    return roleRepository.findByUserType(userType);
  }

  @Override
  public Optional<User> findByNameAndPassword(String name, String password) {
    return userRepository.findUserByUserNameAndPasswordHash(name, password);
  }

  @Override
  public List<Role> findAllRoles() {
    return roleRepository.findAll();
  }

  @Override
  public List<Role> saveAllRoles(List<Role> roles) {
    return roleRepository.saveAll(roles);
  }

}

