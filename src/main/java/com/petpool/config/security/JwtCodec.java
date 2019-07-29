package com.petpool.config.security;

import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtCodec {


  private final static String USER_ID_KEY = "user_id";

  private final static String ROLES_ID_KEY = "roles";

  private Key jwtKey;

  @Autowired
  public JwtCodec(Key jwtKey) {
    this.jwtKey = jwtKey;
  }

  @Data
  @Builder
  public static class Payload {

    private long userId;
    private Set<UserType> roles;
  }

  /**
   * Token was expired
   */
  static class ExpirationTokenException extends Exception {
    ExpirationTokenException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  /**
   * Token has invalid signature( old or fake token)
   */
  static class InvalidSignatureTokenException extends Exception {
    InvalidSignatureTokenException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  /**
   * Token has invalid payload
   */
  static class InvalidPayloadTokenException extends Exception {

    InvalidPayloadTokenException(String message) {
      super(message);
    }
  }

  /**
   * Get date after current to minutes
   *
   * @param minutes time period in minutes
   */
  public static Date createExpirationDateFromNow(int minutes) {
    return createExpirationDateFromDate(new Date(), minutes);
  }

  public static Date createExpirationDateFromDate(Date startDate, int minutes) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(startDate);
    calendar.add(Calendar.MINUTE, minutes);
    return calendar.getTime();
  }

  public String buildToken(User user, int expirationInMinutes, String serviceNameOrUrl) {
    return buildToken(user, createExpirationDateFromNow(expirationInMinutes), serviceNameOrUrl);
  }

  public String buildToken(User user, Date expirationDate, String serviceNameOrUrl) {
    String roles = user.getRoles().stream()
        .map(r -> r.getUserType().getName())
        .collect(Collectors.joining(","));

    Claims claims = Jwts.claims();
    claims.put(USER_ID_KEY, user.getId());
    claims.put(ROLES_ID_KEY, roles);

    String accessToken = Jwts.builder()
        .signWith(jwtKey)
        .setClaims(claims)
        .setExpiration(expirationDate)
        .setSubject(serviceNameOrUrl)
        .setIssuedAt(new Date())
        .compact();
    return accessToken;
  }

  /**
   * Parse token from string and validate it
   *
   * @param token string witch representation of token
   * @return representation of token's payload
   * @throws InvalidPayloadTokenException token has invalid payload
   * @throws InvalidSignatureTokenException token has invalid signature( old or fake token)
   * @throws ExpirationTokenException token was expired
   */
  public Payload parseToken(String token)
      throws InvalidPayloadTokenException, InvalidSignatureTokenException, ExpirationTokenException {
    Claims body;
    try {
      body = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
    } catch (ExpiredJwtException ex) {
      throw new ExpirationTokenException("Token is expired.", ex);
    } catch (JwtException ex) {
      throw new InvalidSignatureTokenException("Token is invalid.", ex);
    }

    if (!body.containsKey(USER_ID_KEY) || !body.containsKey(ROLES_ID_KEY)) {
      throw new InvalidPayloadTokenException("The required key was not found.");
    }

    long userId;
    Object userObject = body.get(USER_ID_KEY);
    if (userObject instanceof Integer) {
      userId = (Integer) userObject;
    } else {
      userId = (Long) userObject;
    }

    String[] split = ((String) body.get(ROLES_ID_KEY)).split(",");

    Set<UserType> userTypes = UserType.byNames(split);
    if (userTypes.isEmpty()) {
      userTypes.add(UserType.NOT_APPROVED);
    }

    return Payload.builder().userId(userId).roles(userTypes).build();
  }


}
