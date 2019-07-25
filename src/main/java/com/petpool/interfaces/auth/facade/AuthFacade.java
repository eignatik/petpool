package com.petpool.interfaces.auth.facade;

import com.petpool.domain.model.user.User;
import java.util.Map;
import java.util.Optional;

public interface AuthFacade {

  User createNewUser(User user);

  Optional<User> findById(Long id);

  Optional<Map<String, String>> requestTokenForUser(String name, String password, String userAgent);

  Optional<Map<String, String>> refreshTokenForUser(String refreshToken, String userAgent);
}
