package com.petpool.interfaces.auth.facade;

import com.petpool.application.constants.TokenAttributes;
import com.petpool.domain.model.user.Token;
import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacadeImpl implements AuthFacade {

  private final UserService userService;

  private PasswordEncoder passwordEncoder;

  @Autowired
  public void setPasswordEncoder(
      PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public AuthFacadeImpl(UserService userService) {
    this.userService = userService;
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
        .map(user -> {
          if (checkPassword(credentials.getPassword(), user.getPasswordHash())) {
            return user;
          } else {
            return null;
          }
        })
        .map(user -> generateToken(user, userAgent, "1.1.1.1"));
  }

  //TODO: проверять дату окончания токена, если не соответствует, то нужно заново залогиниться
  @Override
  public Optional<Map<String, String>> refreshTokenForUser(String refreshToken, String userAgent) {
    return userService
        .findTokenByRefreshToken(refreshToken)
        .map(token -> updateToken(token, userAgent, "1.1.1.1"));
  }

  private boolean checkPassword(String password, String encodedPassword) {
    return passwordEncoder.matches(password, encodedPassword);
  }

  private Map<String, String> generateToken(User user, String userAgent, String ip) {

    Date expirationDate = createExpirationDate();
    Map<String, String> res = buildNewTokens(user, expirationDate);

    Token token = new Token(
        res.get(TokenAttributes.REFRESH_TOKEN),
        expirationDate,
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
    Date newExpired = createExpirationDate();

    Map<String, String> res = buildNewTokens(token.getUser(), newExpired);

    token.setBrowser(browserFromUserAgent(userAgent));
    token.setIp(ip);
    token.setUserAgent(userAgent);
    token.setOs(osFromUserAgent(userAgent));
    token.setExpired(newExpired);
    token.setToken(res.get(TokenAttributes.REFRESH_TOKEN));

    userService.saveToken(token);

    return res;
  }

  private Date createExpirationDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.add(Calendar.MONTH, 1);
    return calendar.getTime();
  }

  private Map<String, String> buildNewTokens(User user, Date expired) {
    //TODO: temporal stub for access token, switch with JWT
    String accessToken =
        user.getId() + ":" + expired.getTime() + ":" + user.getRoles().stream()
            .map(r -> r.getUserType().getValue()).collect(
                Collectors.joining(","));

    String refreshToken = UUID.randomUUID().toString();

    Map<String, String> map = new HashMap<>();

    map.put(TokenAttributes.ACCESS_TOKEN,
        Base64.getEncoder().encodeToString(accessToken.getBytes()));
    map.put(TokenAttributes.REFRESH_TOKEN,
        Base64.getEncoder().encodeToString(refreshToken.getBytes()));
    map.put(TokenAttributes.EXPIRED, String.valueOf(expired.getTime()));
    return map;
  }

  private String osFromUserAgent(String userAgent) {
    return "windows";
  }

  private String browserFromUserAgent(String userAgent) {
    return "firefox";
  }
}
