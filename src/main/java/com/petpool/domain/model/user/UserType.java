package com.petpool.domain.model.user;

public enum UserType {
  ADMIN("ADMIN"),
  USER("USER"),
  NOT_APPROVED("NOT_APPROVED"),
  BANNED("BANNED"),
  DELETED("DELETED");

  private String value;

  UserType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
