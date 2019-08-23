package com.petpool.domain.model.user;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

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

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
  private Person person;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
      name="user_tokens",
      joinColumns = @JoinColumn( name="user_id"),
      inverseJoinColumns = @JoinColumn( name="token_id")
  )
  private Set<Token> tokens;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name="user_roles",
      joinColumns = @JoinColumn( name="user_id"),
      inverseJoinColumns = @JoinColumn( name="role_id")
  )
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
