package com.petpool.config.security;

import com.petpool.domain.model.user.UserType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthorizedUser implements UserDetails {

  private final long userId;
  private final Set<UserType> roles;
  private final Set<SimpleGrantedAuthority> authority = new HashSet<>();

  public AuthorizedUser(long userId, Set<UserType> roles) {
    this.userId = userId;
    this.roles = roles;
    for (UserType role : roles) {
      authority.add(new SimpleGrantedAuthority(role.getName()));
    }

  }

  public long getUserId() {
    return userId;
  }

  public Set<UserType> getRoles() {
    return roles;
  }

  public boolean hasRole(UserType role){
      return roles.contains(role);
  }

  public boolean hasAnyRoles(UserType ... roles){
    if(roles.length==0) return false;
    for (UserType role : roles) {
        if(this.roles.contains(role)) return true;
    }
    return false;
  }

  public boolean hasAllRoles(UserType ... roles){
    if(roles.length==0) return false;
    for (UserType role : roles) {
      if(!this.roles.contains(role)) return false;
    }
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authority;

  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return String.valueOf(userId);
  }

  @Override
  public boolean isAccountNonExpired() {
    return !userIsBannedOrDeleted();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !userIsBannedOrDeleted();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !userIsBannedOrDeleted();
  }

  @Override
  public boolean isEnabled() {
    return !userIsBannedOrDeleted();
  }

  private boolean userIsBannedOrDeleted(){
    return roles.contains(UserType.BANNED)||  roles.contains(UserType.DELETED);
  }
}
