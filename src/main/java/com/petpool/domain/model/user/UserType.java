package com.petpool.domain.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_type")
public class UserType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(mappedBy = "userType")
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_type")
  private UserTypes userType;

  public UserType(UserTypes userType) {
    this.userType = userType;
  }

}
