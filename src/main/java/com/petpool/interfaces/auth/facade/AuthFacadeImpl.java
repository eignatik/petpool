package com.petpool.interfaces.auth.facade;

import com.petpool.application.constants.TokenAttributes;
import com.petpool.config.security.JwtCodec;
import com.petpool.domain.model.user.Token;
import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacadeImpl implements AuthFacade {

  private final UserService userService;

  private PasswordEncoder passwordEncoder;

  private Key jwtKey;

  private JwtCodec jwtCodec;

  @Value("${serviceName}")
  private String serviceName;

  @Value("${token.expirationInMinutes}")
  private int expirationTokenTime;

  @Value("${token.refresh.expirationInMinutes}")
  private int expirationRefreshTokenTime;

  @Autowired
  public AuthFacadeImpl(
      UserService userService,
      PasswordEncoder passwordEncoder,
      Key jwtKey,
      JwtCodec jwtCodec) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtKey = jwtKey;
    this.jwtCodec = jwtCodec;
  }

  private User deleteExpiredTokens(User user) {
    userService.deleteExpiredTokens(user);
    return user;
  }

  @Override
  public Optional<Map<String, String>> requestTokenForUser(Credentials credentials,
      String userAgent) {
    Optional<User> foundedUser;

    if (!StringUtils.isEmpty(credentials.getEmail())) {
      foundedUser = userService.findByEmail(credentials.getEmail());
    } else {
      foundedUser = userService.findByName(credentials.getName());
    }

    return foundedUser
        .map(this::deleteExpiredTokens)
        .map(user -> {
          if (checkPassword(credentials.getPassword(), user.getPasswordHash())) {
            return user;
          } else {
            return null;
          }
        })
        .map(user -> generateToken(user, userAgent, "1.1.1.1"));
  }

  /**
   * Refresh access token by refresh token
   *
   * if token expired method return Options.empty and user must request new  token!
   *
   * @param refreshToken token to be refreshed
   * @param userAgent user agent
   */
  @Override
  public Optional<Map<String, String>> refreshTokenForUser(String refreshToken, String userAgent) {
    return userService
        .findTokenByRefreshToken(refreshToken)
        .flatMap(token -> {
          if (checkRefreshTokenNotExpired(token)) {
            return Optional.of(token);
          }
          userService.removeToken(token);
          return Optional.empty();
        })
        .map(token -> updateToken(token, userAgent, "1.1.1.1"));
  }

  private boolean checkPassword(String password, String encodedPassword) {
    return passwordEncoder.matches(password, encodedPassword);
  }

  private boolean checkRefreshTokenNotExpired(Token token) {
    return token.getExpired().after(new Date(System.currentTimeMillis()));
  }

  private Map<String, String> generateToken(User user, String userAgent, String ip) {

    Date expirationDateRefresh = JwtCodec.createExpirationDateFromNow(expirationRefreshTokenTime);
    Map<String, String> res = buildNewTokens(user);

    Token token = new Token(
        res.get(TokenAttributes.REFRESH_TOKEN),
        expirationDateRefresh,
        new Date(System.currentTimeMillis()),
        ip,
        userAgent,
        osFromUserAgent(userAgent),
        browserFromUserAgent(userAgent),
        user
    );

    userService.saveToken(token);

    return res;
  }

  private Map<String, String> updateToken(Token token, String userAgent, String ip) {

    Date expirationDateRefresh = JwtCodec.createExpirationDateFromNow(expirationRefreshTokenTime);
    Map<String, String> res = buildNewTokens(token.getUser());

    token.setBrowser(browserFromUserAgent(userAgent));
    token.setIp(ip);
    token.setUserAgent(userAgent);
    token.setOs(osFromUserAgent(userAgent));
    token.setExpired(expirationDateRefresh);
    token.setToken(res.get(TokenAttributes.REFRESH_TOKEN));

    userService.saveToken(token);

    return res;
  }


  private Map<String, String> buildNewTokens(User user) {

    Date expirationDate = JwtCodec.createExpirationDateFromNow(expirationTokenTime);
    String accessToken = jwtCodec.buildToken(user, expirationDate, serviceName);

    String refreshToken = UUID.randomUUID().toString();

    Map<String, String> map = new HashMap<>();

    map.put(TokenAttributes.ACCESS_TOKEN, accessToken);
    map.put(TokenAttributes.REFRESH_TOKEN,
        Base64.getEncoder().encodeToString(refreshToken.getBytes()));
    map.put(TokenAttributes.EXPIRED, String.valueOf(expirationDate.getTime()));
    return map;
  }

  private String osFromUserAgent(String userAgent) {
    return "windows";
  }

  private String browserFromUserAgent(String userAgent) {
    return "firefox";
  }


}
