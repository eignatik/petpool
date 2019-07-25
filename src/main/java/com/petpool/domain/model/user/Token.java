package com.petpool.domain.model.user;

import com.petpool.domain.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Version
  private Long version;

  @Column(name = "refresh_token", unique = true, nullable = false)
  private String token;

  private Date expired;

  private Date created;

  @Column(nullable = false)
  private String ip;

  @Column(name = "user_agent")
  private String userAgent;

  @Column(name = "os", length = 10)
  private String os;

  @Column(length = 16)
  private String browser;

  public Token() {

  }

}
