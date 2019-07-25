package com.petpool.interfaces.auth.facade;

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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthImpl implements AuthFacade {

  private final UserService userService;

  private PasswordEncoder passwordEncoder;

  @Autowired
  public void setPasswordEncoder(
      PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Autowired
  public AuthImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public User createNewUser(User user) {
    return userService.saveUser(user);
  }

  @Override
  public Optional<User> findById(Long id) {
    return userService.findById(id);
  }

  @Override
  public Optional<Map<String, String>> requestTokenForUser(Credentials credentials,
      String userAgent) {
    Optional<User> foundedUser;

    if(!credentials.getEmail().isEmpty()) foundedUser = userService.findByEmail(credentials.getEmail());
    else foundedUser = userService.findByName(credentials.getName());

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

    Date newExpired = buildExpiredDate();
    Map<String, String> res = buildNewTokens(user, newExpired);

    Token token = new Token(
        res.get("refreshToken"),
        newExpired,
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
    Date newExpired = buildExpiredDate();

    Map<String, String> res = buildNewTokens(token.getUser(), newExpired);

    token.setBrowser(browserFromUserAgent(userAgent));
    token.setIp(ip);
    token.setUserAgent(userAgent);
    token.setOs(osFromUserAgent(userAgent));
    token.setExpired(newExpired);
    token.setToken(res.get("refreshToken"));

    userService.saveToken(token);

    return res;
  }

  private Date buildExpiredDate(){
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(System.currentTimeMillis());
    c.add(Calendar.WEEK_OF_MONTH, 1);
    return c.getTime();
  }

  private Map<String, String> buildNewTokens(User user, Date expired){

    String accessToken =
        user.getId() + ":" + expired.getTime() + ":" +  user.getRoles().stream()
            .map(r -> r.getUserType().getValue()).collect(
                Collectors.joining(","));

    String refreshToken = UUID.randomUUID().toString();

    Map<String, String> map = new HashMap<>();

    map.put("accessToken",
        Base64.getEncoder().encodeToString(accessToken.getBytes()));
    map.put("refreshToken",
        Base64.getEncoder().encodeToString(refreshToken.getBytes()));
    map.put("expired", String.valueOf(expired.getTime()));
    return map;
  }

  private String osFromUserAgent(String userAgent) {
    return "windows";
  }

  private String browserFromUserAgent(String userAgent) {
    return "firefox";
  }
}
