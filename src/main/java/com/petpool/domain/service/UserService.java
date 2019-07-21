package com.petpool.domain.service;

import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import java.util.List;

public interface UserService {

  User createUser(User user);

  User findById(Long id);

  User findByLogin(String login);

  User findByEmail(String email);

  Role findRoleByType(UserType userType);

  List<Role> findAllRoles();

  List<Role> saveAllRoles(List<Role> roles);

}
