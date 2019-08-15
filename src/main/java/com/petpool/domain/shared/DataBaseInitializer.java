package com.petpool.domain.shared;

import com.petpool.domain.model.user.Person;
import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import com.petpool.domain.service.PersonService;
import com.petpool.domain.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInitializer {

  private final UserService userService;
  private final PersonService personService;
  private final PasswordEncoder encoder;

  @Autowired
  public DataBaseInitializer(UserService userService, PersonService personService, PasswordEncoder encoder) {
    this.userService = userService;
    this.personService = personService;
    this.encoder = encoder;
  }

  /**
   * TODO: write docs
   */
  public void init() {
    initRoles();
  }

  private void initRoles() {
    List<Role> rolesToSave;

    List<UserType> existingRoles = userService.findAllRoles()
        .stream()
        .map(Role::getUserType)
        .collect(Collectors.toList());

    rolesToSave = Stream.of(UserType.values())
        .filter(type -> !existingRoles.contains(type))
        .map(Role::new)
        .collect(Collectors.toList());

    if (CollectionUtils.isNotEmpty(rolesToSave)) {
      userService.saveAllRoles(rolesToSave);
    }

    Role adminRole = userService.findRoleByType(UserType.ADMIN)
        .orElseThrow(() -> new RuntimeException("Нет такой роли пользователя"));
    Role userRole = userService.findRoleByType(UserType.USER)
        .orElseThrow(() -> new RuntimeException("Нет такой роли пользователя"));

    User user = new User(
        "root",
        encoder.encode("123"),
        "root@test.test",
        new Date(System.currentTimeMillis()),
        Set.of(adminRole, userRole));
    userService.saveUser(user);

    user = new User(
        "user",
        encoder.encode("123"),
        "user@test.test",
        new Date(System.currentTimeMillis()),
        Set.of(userRole));
    userService.saveUser(user);

    var person = new Person(
            "John",
            "Doe",
            "Saint Petersburg",
            "ru",
            user);
    personService.savePerson(person);

   // eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiVVNFUixBRE1JTiJ9.6p3hItj37M_Aw7M0rNiHSe6bHhdGNgH8dNZ2Ol_Tw28
   // eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiVVNFUixBRE1JTiJ9.6p3hItj37M_Aw7M0rNiHSe6bHhdGNgH8dNZ2Ol_Tw28
  }

}
