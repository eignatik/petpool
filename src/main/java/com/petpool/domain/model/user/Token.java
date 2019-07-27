package com.petpool.domain.model.user;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import lombok.Data;

@Data
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

  @Temporal(value = TemporalType.DATE)
  private Date expired;

  @Temporal(value = TemporalType.DATE)
  private Date created;

  @Column(nullable = false)
  private String ip;

  @Column(name = "user_agent")
  private String userAgent;

  @Column(name = "os", length = 10)
  private String os;

  @Column(length = 16)
  private String browser;

  @ManyToOne()
  private User user;

  public Token() {

  }

  public Token( String token, Date expired, Date created, String ip,
      String userAgent, String os, String browser, User user) {
    this.token = token;
    this.expired = expired;
    this.created = created;
    this.ip = ip;
    this.userAgent = userAgent;
    this.os = os;
    this.browser = browser;
    this.user = user;
  }
}
