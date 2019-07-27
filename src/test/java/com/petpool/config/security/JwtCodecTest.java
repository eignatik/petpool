package com.petpool.config.security;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.petpool.config.security.JwtCodec.ExpirationTokenException;
import com.petpool.config.security.JwtCodec.InvalidPayloadTokenException;
import com.petpool.config.security.JwtCodec.InvalidSignatureTokenException;
import com.petpool.config.security.JwtCodec.Payload;
import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Set;
import javax.crypto.SecretKey;
import org.testng.annotations.Test;

public class JwtCodecTest {

  @Test
  public void testBuildAndParseToken()
      throws InvalidPayloadTokenException, InvalidSignatureTokenException, ExpirationTokenException {
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    JwtCodec codec = new JwtCodec();
    codec.setJwtKey(key);

    Set<Role> roles = Set.of(new Role(UserType.ADMIN), new Role(UserType.USER));
    User user = new User("Вася", "qwe", "qwe@qwe.com", new Date(), roles);
    user.setId(1L);

    String token = codec.buildToken(user, 60, "service");

    assertNotNull(token);

    Payload payload = codec.parseToken(token);

    assertEquals(payload.getUserId(), 1);

    assertTrue(payload.getRoles().stream()
        .filter(r -> r.getName().equals("ADMIN") || r.getName().equals("USER")).count() == 2);
  }


  @Test
  public void testCreateExpirationDate() {

    Date expirationDate = JwtCodec.createExpirationDate(100);
    long delta = expirationDate.getTime() - System.currentTimeMillis();
    long expiration = 100 * 60 * 1000;

    assertTrue(expiration - delta >= 0 && expiration - delta < 1000 * 2L);
  }
}
