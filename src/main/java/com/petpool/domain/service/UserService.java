package com.petpool.domain.service;

import com.petpool.domain.model.user.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

  Person savePerson(Person person);

  User saveUser(User user);

  Optional<User> findById(Long id);

  Optional<User> findByName(String login);

  Optional<User> findByEmail(String email);

  Optional<User> findByNameAndPassword(String name, String password);

  Optional<User> findUserByToken(String token);

  Optional<Role> findRoleByType(UserType userType);

  List<Role> findAllRoles();

  List<Role> saveAllRoles(List<Role> roles);

  Token saveToken(Token token);

  void removeToken(Token token);

  Optional<Token> findTokenByRefreshToken(String token);

  void deleteExpiredTokens(User user);

}
