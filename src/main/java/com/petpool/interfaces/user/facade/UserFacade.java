package com.petpool.interfaces.user.facade;

import com.petpool.domain.model.user.User;
import java.util.Map;
import java.util.Optional;

public interface UserFacade {
  Optional<User> findById(Long id);
}
