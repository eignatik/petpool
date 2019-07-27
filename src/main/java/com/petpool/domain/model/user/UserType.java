package com.petpool.domain.model.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public enum UserType {
  ADMIN("ADMIN"),
  USER("USER"),
  NOT_APPROVED("NOT_APPROVED"),
  BANNED("BANNED"),
  DELETED("DELETED");

  private String name;

  UserType(String value) {
    this.name = value;
  }

  public String getName() {
    return name;
  }

  public static Optional<UserType> byName(String name) {
    for (UserType value : UserType.values()) {
      if (value.getName().equals(name)) {
        return Optional.of(value);
      }
    }
    return Optional.empty();
  }

  public static Set<UserType> byNames(String... names) {
    Set<String> namesSet = Set.of(names);
    Set<UserType> res = new HashSet<>(names.length);

    for (UserType value : UserType.values()) {
      if (namesSet.contains(value.getName())) res.add(value);
    }
    return res;
  }


}
