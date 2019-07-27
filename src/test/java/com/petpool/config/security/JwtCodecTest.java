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

    JwtCodec codec = new JwtCodec(key);

    Set<Role> roles = Set.of(new Role(UserType.ADMIN), new Role(UserType.USER));
    User user = new User("Вася", "qwe", "qwe@qwe.com", new Date(), roles);
    user.setId(1L);

    String token = codec.buildToken(user, 60, "service");

    Payload payload = codec.parseToken(token);

    assertEquals(payload.getUserId(), 1);

    assertTrue(hasAdminAndUserInPayloadRoles(payload),"Payload dont contains serialized roles");
  }
  private boolean hasAdminAndUserInPayloadRoles(Payload payload){
    return payload.getRoles().stream()
        .filter(r -> r.getName().equals("ADMIN") || r.getName().equals("USER")).count() == 2;
  }


  @Test
  public void testCreateExpirationDate() {

    int testedTimeInMinutes=100;
    long testedTimeInMilliseconds = 100 * 60 * 1000;
    Date currentTime = new Date();
    Date expectedDate = new Date(currentTime.getTime()+testedTimeInMilliseconds);

    Date actualDate = JwtCodec.createExpirationDateFromDate(currentTime, testedTimeInMinutes);

    assertTrue(actualDate.compareTo(expectedDate)==0, "Expected and actual date not equals");
  }
}
