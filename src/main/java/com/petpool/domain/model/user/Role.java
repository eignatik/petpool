package com.petpool.domain.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.petpool.domain.shared.BaseEntity;
import lombok.Data;

@Data
@Table(name = "role")
public class Role extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type", unique = true, nullable = false)
  private UserType userType;

  public Role() {

  }

  public Role(UserType userType) {
    this.userType = userType;
  }

}
