package com.petpool.config.security;


import com.petpool.domain.model.user.UserType;
import java.io.Serializable;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;

public class AuthorizedUser implements Serializable {

  private final long userId;
  private final Set<UserType> userRoles;


  public static final AuthorizedUser GUEST = new AuthorizedUser(0, Set.of(UserType.GUEST));

  public AuthorizedUser(long userId, Set<UserType> userRoles) {
    this.userId = userId;
    this.userRoles = userRoles;
  }

  public long getUserId() {
    return userId;
  }

  public Set<UserType> getRoles() {
    return userRoles;
  }

  public boolean hasRole(UserType role) {
    return userRoles.contains(role);
  }

  public boolean hasAnyRoles(UserType... roles) {
    if (roles.length == 0) {
      return false;
    }
    return CollectionUtils.containsAny(userRoles, roles);
  }

  public boolean hasAllRoles(UserType... roles) {
    if (roles.length == 0) {
      return false;
    }
    for (UserType role : roles) {
      if (!userRoles.contains(role)) {
        return false;
      }
    }
    return true;
  }
}
