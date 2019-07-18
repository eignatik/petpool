package com.petpool.domain.model.user;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "user_name")
  private String userName;

  @Column(name = "password_hash")
  private String passwordHash;

  private String email;

  @Column(name = "last_login")
  private Date lastLogin;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(unique = true)
  private UserType userType;

  public User(String userName, String passwordHash, String email, Date lastLogin,
      UserType userType) {
    this.userName = userName;
    this.passwordHash = passwordHash;
    this.email = email;
    this.lastLogin = lastLogin;
    this.userType = userType;
  }

}
