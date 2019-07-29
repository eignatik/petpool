package com.petpool.config.security;

import lombok.Data;

@Data
public class SecurityConf {
  private String tokenProviderName;
  private int accessTokenExpirationInMinutes;
  private int refreshTokenExpirationInMinutes;
}
