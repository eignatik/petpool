package com.petpool.domain.shared;

import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.RoleRepository;
import com.petpool.domain.model.user.UserType;
import com.petpool.domain.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInitializer {

  private final UserService userService;

  @Autowired
  public DataBaseInitializer(UserService userService) {
    this.userService = userService;
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
  }

}
