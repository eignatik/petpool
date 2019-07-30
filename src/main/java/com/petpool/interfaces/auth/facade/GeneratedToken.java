package com.petpool.interfaces.auth.facade;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneratedToken implements Serializable {
  private String accessToken;
  private String refreshToken;
  private long expired;

  public GeneratedToken() {
  }
}
