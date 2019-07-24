package com.petpool.domain.model.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByUserName(String userName);

  Optional<User> findUserByUserNameAndPasswordHash(String userName, String passwordHash);

}
