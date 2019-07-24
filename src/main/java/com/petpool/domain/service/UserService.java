package com.petpool.domain.service;

import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import java.util.List;
import java.util.Optional;

public interface UserService {

  User createUser(User user);

  Optional<User> findById(Long id);

  Optional<User> findByLogin(String login);

  Optional<User> findByEmail(String email);

  Optional<Role> findRoleByType(UserType userType);

  Optional<User> findByNameAndPassword(String name, String password);

  List<Role> findAllRoles();

  List<Role> saveAllRoles(List<Role> roles);

}
