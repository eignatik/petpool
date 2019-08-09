package com.petpool.interfaces.auth.facade;

import com.petpool.application.util.ip.IpParser;
import com.petpool.application.util.useragent.UserAgentParser;
import com.petpool.application.util.useragent.UserAgentParserResult;
import com.petpool.config.security.JwtCodec;
import com.petpool.config.security.SecurityConf;
import com.petpool.domain.model.user.Token;
import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacadeImpl implements AuthFacade {

  private final UserService userService;

  private final PasswordEncoder passwordEncoder;

  private final JwtCodec jwtCodec;

  private final SecurityConf securityConf;

  private final UserAgentParser userAgentParser;

  private final IpParser ipParser;


  @Autowired
  public AuthFacadeImpl(
      UserService userService,
      PasswordEncoder passwordEncoder,
      JwtCodec jwtCodec, SecurityConf securityConf,
      UserAgentParser userAgentParser, IpParser ipParser) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtCodec = jwtCodec;
    this.securityConf = securityConf;
    this.userAgentParser = userAgentParser;
    this.ipParser = ipParser;
  }

  private User deleteExpiredTokens(User user) {
    userService.deleteExpiredTokens(user);
    return user;
  }

  @Override
  public Optional<GeneratedToken> requestTokenForUser(Credentials credentials,
      HttpHeaders headers) {
    Optional<User> foundedUser;

    if (!StringUtils.isEmpty(credentials.getEmail())) {
      foundedUser = userService.findByEmail(credentials.getEmail());
    } else {
      foundedUser = userService.findByName(credentials.getName());
    }
    String userAgent = Optional.ofNullable(headers.get("User-Agent"))
        .map(l -> l.get(0)).orElse("");

    return foundedUser
        .map(this::deleteExpiredTokens)
        .map(user -> {
          if (checkPassword(credentials.getPassword(), user.getPasswordHash())) {
            return user;
          } else {
            return null;
          }
        })
        .map(user -> generateToken(user, userAgent, ipParser.parse(headers)));
  }

  /**
   * Refresh access token by refresh token
   *
   * if token expired method return Options.empty and user must request new  token!
   *
   * @param refreshToken token to be refreshed
   * @param headers http headers
   */
  @Override
  public Optional<GeneratedToken> refreshTokenForUser(String refreshToken, HttpHeaders headers) {
    String userAgent = Optional.ofNullable(headers.get("User-Agent"))
        .map(l -> l.get(0)).orElse("");
    return userService
        .findTokenByRefreshToken(refreshToken)
        .flatMap(token -> {
          if (checkRefreshTokenNotExpired(token)) {
            return Optional.of(token);
          }
          userService.removeToken(token);
          return Optional.empty();
        })
        .map(token -> updateToken(token, userAgent, ipParser.parse(headers)));
  }

  @Override
  public Boolean isUniqueByUserName(String name) {
    return userService.findByName(name).isEmpty();
  }

  @Override
  public Boolean isUniqueByEmail(String email) {
    return userService.findByEmail(email).isEmpty();
  }

  private boolean checkPassword(String password, String encodedPassword) {
    return passwordEncoder.matches(password, encodedPassword);
  }

  private boolean checkRefreshTokenNotExpired(Token token) {
    return token.getExpired().after(new Date(System.currentTimeMillis()));
  }

  private GeneratedToken generateToken(User user, String userAgent, String ip) {

    Date expirationDateRefresh = JwtCodec
        .createExpirationDateFromNow(securityConf.getRefreshTokenExpirationInMinutes());
    GeneratedToken res = buildNewTokens(user);

    UserAgentParserResult userAgentParserResult = userAgentParser.parse(userAgent);

    Token token = new Token(
        res.getRefreshToken(),
        expirationDateRefresh,
        new Date(System.currentTimeMillis()),
        ip,
        userAgent,
        userAgentParserResult.getOs().toString(),
        userAgentParserResult.getBrowser().toString(),
        user
    );

    userService.saveToken(token);

    return res;
  }

  private GeneratedToken updateToken(Token token, String userAgent, String ip) {

    Date expirationDateRefresh = JwtCodec
        .createExpirationDateFromNow(securityConf.getRefreshTokenExpirationInMinutes());
    GeneratedToken res = buildNewTokens(token.getUser());

    UserAgentParserResult userAgentParserResult = userAgentParser.parse(userAgent);

    token.setBrowser(userAgentParserResult.getBrowser().toString());
    token.setIp(ip);
    token.setUserAgent(userAgent);
    token.setOs(userAgentParserResult.getOs().toString());
    token.setExpired(expirationDateRefresh);
    token.setToken(res.getRefreshToken());

    userService.saveToken(token);

    return res;
  }


  private GeneratedToken buildNewTokens(User user) {

    Date expirationDate = JwtCodec
        .createExpirationDateFromNow(securityConf.getAccessTokenExpirationInMinutes());
    String accessToken = jwtCodec
        .buildToken(user, expirationDate, securityConf.getTokenProviderName());

    String refreshToken = UUID.randomUUID().toString();

    GeneratedToken newToken = new GeneratedToken(
        accessToken,
        Base64.getEncoder().encodeToString(refreshToken.getBytes()),
        expirationDate.getTime()
    );

    return newToken;
  }

}
