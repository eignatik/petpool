package com.petpool.config.security;


import com.petpool.domain.model.user.UserType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthorizedUserAuthentication implements Authentication {

  private final AuthorizedUser user;

  private final Set<SimpleGrantedAuthority> authority = new HashSet<>();

  public AuthorizedUserAuthentication(AuthorizedUser user) {
    this.user = user;

    for (UserType role : user.getRoles()) {
      authority.add(new SimpleGrantedAuthority(role.getName()));
    }
  }

  public AuthorizedUser getUser() {
    return user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authority;

  }

  @Override
  public Object getCredentials() {
    return user.getUserId();
  }

  @Override
  public Object getDetails() {
    return user.getUserId();
  }

  @Override
  public Object getPrincipal() {
    return user.getUserId();
  }

  @Override
  public boolean isAuthenticated() {
    return userIsBannedOrDeletedOrGuest();
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    throw new RuntimeException("Operation not used");
  }


  private boolean userIsBannedOrDeletedOrGuest() {
    return CollectionUtils
        .containsAny(user.getRoles(), UserType.BANNED, UserType.DELETED, UserType.GUEST);
  }

  @Override
  public String getName() {
    return String.valueOf(user.getUserId());
  }
}
