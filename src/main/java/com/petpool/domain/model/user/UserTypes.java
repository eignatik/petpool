package com.petpool.domain.model.user;

public enum UserTypes {
  ADMIN("admin"),
  USER("user");

  private String value;

  UserTypes(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
