package com.petpool.config.security;


import com.petpool.domain.model.user.UserType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthorizedUser implements UserDetails {

  private final long userId;
  private final Set<UserType> userRoles;
  private final Set<SimpleGrantedAuthority> authority = new HashSet<>();

  public static final AuthorizedUser GUEST = new AuthorizedUser(0, Set.of(UserType.GUEST));

  public AuthorizedUser(long userId, Set<UserType> userRoles) {
    this.userId = userId;
    this.userRoles = userRoles;
    for (UserType role : userRoles) {
      authority.add(new SimpleGrantedAuthority(role.getName()));
    }

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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authority;

  }

  @Override
  public String getPassword() {
    return StringUtils.EMPTY;//not needed
  }

  @Override
  public String getUsername() {
    return String.valueOf(userId);
  }

  @Override
  public boolean isAccountNonExpired() {
    return !userIsBannedOrDeletedOrGuest();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !userIsBannedOrDeletedOrGuest();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !userIsBannedOrDeletedOrGuest();
  }

  @Override
  public boolean isEnabled() {
    return !userIsBannedOrDeletedOrGuest();
  }

  private boolean userIsBannedOrDeletedOrGuest() {
    return CollectionUtils.containsAny(userRoles, UserType.BANNED, UserType.DELETED, UserType.GUEST);
  }
}
