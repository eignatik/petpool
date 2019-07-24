package com.petpool.domain.model.user;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  Optional<Token> findOneByToken(String token);
  List<Token> findAllByExpiredBefore(Date date);

}
