package com.petpool.interfaces.auth.facade;

import com.petpool.domain.model.user.User;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;

public interface AuthFacade {

  User createNewUser(User user);

  Optional<User> findById(Long id);

  Optional<Map<String, String>> requestTokenForUser(Credentials credentials, String userAgent);

  Optional<Map<String, String>> refreshTokenForUser(String refreshToken, String userAgent);

  @Data
  @AllArgsConstructor
  class Credentials {

    private String email;
    private String name;
    private String password;

    public Credentials() {
    }

    public boolean isValid() {
      return !(email.isEmpty() && name.isEmpty() || password.isEmpty());
    }
  }
}
