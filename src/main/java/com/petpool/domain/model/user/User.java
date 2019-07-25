package com.petpool.domain.model.user;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.petpool.domain.shared.BaseEntity;
import javax.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Version
  private Long version;
  @Column(name = "user_name", unique = true, nullable = false, length = 20)
  private String userName;

  @Column(name = "password_hash")
  private String passwordHash;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "last_login")
  private Date lastLogin;

  @OneToMany(cascade = CascadeType.ALL)
  private Set<Token> tokens;

  @OneToMany(cascade = CascadeType.ALL)
  private Set<Role> roles;

  public User() {

  }

  public User(String userName, String passwordHash, String email, Date lastLogin,
      Set<Role> roles) {
    this.userName = userName;
    this.passwordHash = passwordHash;
    this.email = email;
    this.lastLogin = lastLogin;
    this.roles = roles;
  }

}
