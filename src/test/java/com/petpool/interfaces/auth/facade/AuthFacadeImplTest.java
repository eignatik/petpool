package com.petpool.interfaces.auth.facade;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.petpool.application.util.ip.IpParser;
import com.petpool.application.util.useragent.Browser;
import com.petpool.application.util.useragent.OS;
import com.petpool.application.util.useragent.UserAgentParser;
import com.petpool.application.util.useragent.UserAgentParserResult;
import com.petpool.config.security.JwtCodec;
import com.petpool.config.security.SecurityConf;
import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.Token;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import com.petpool.domain.service.UserService;
import com.petpool.interfaces.auth.facade.AuthFacade.Credentials;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AuthFacadeImplTest {

  private static final String TEST_NAME = "testName";
  private static final String TEST_EMAIL = "hello@test.test";
  private static final String TEST_PASSWORD = "secretPassword";
  private static final String USER_AGENT = "userAgent";
  private static final Integer EXPIRATION_STUB_TIME = 10000;
  private static final String TEST_PROVIDER_NAME = "testProviderName";

  private static final HttpHeaders headers = new HttpHeaders();

  @Mock
  private UserAgentParser userAgentParser;
  @Mock
  private UserService userService;
  @Mock
  private PasswordEncoder encoder;
  @Mock
  private SecurityConf securityConf;
  @Mock
  private JwtCodec jwtCodec;
  @Mock
  IpParser ipParser;

  @InjectMocks
  private AuthFacadeImpl facade;

  @BeforeClass
  public void init() {
    MockitoAnnotations.initMocks(this);
    when(securityConf.getAccessTokenExpirationInMinutes()).thenReturn(EXPIRATION_STUB_TIME);
    when(securityConf.getTokenProviderName()).thenReturn(TEST_PROVIDER_NAME);
    headers.set("User-Agent", USER_AGENT);
  }

  @AfterMethod
  public void tearDown() {
    reset(userService, encoder);
  }

  @DataProvider(name = "credentials")
  public Object[][] getCredentials() {
    return new Object[][]{
        {new Credentials(TEST_NAME, TEST_EMAIL, TEST_PASSWORD)},
        {new Credentials(StringUtils.EMPTY, TEST_EMAIL, TEST_PASSWORD)},
        {new Credentials(TEST_NAME, StringUtils.EMPTY, TEST_PASSWORD)}
    };
  }

  @Test(dataProvider = "credentials")
  public void testRequestTokenForUser_generatesTokens_whenThereIsUser(Credentials credentials) {
    User user = new User();
    user.setId(1L);
    user.setRoles(Set.of(new Role(UserType.USER)));
    user.setPasswordHash(TEST_PASSWORD);
    when(userService.findByEmail(eq(credentials.getEmail()))).thenReturn(Optional.of(user));
    when(userService.findByName(eq(credentials.getName()))).thenReturn(Optional.of(user));
    when(encoder.matches(eq(TEST_PASSWORD), eq(TEST_PASSWORD))).thenReturn(true);
    when(userAgentParser.parse(anyString()))
        .thenReturn(getStubUserAgentParserResult());
    when(ipParser.parse(any(HttpHeaders.class)))
        .thenReturn(USER_AGENT);

    Optional<GeneratedToken> tokens = facade
        .requestTokenForUser(credentials, headers);

    Assert.assertNotNull(tokens.orElse(null), "Tokens should be present");
  }

  @Test(dataProvider = "credentials")
  public void testRequestTokenForUser_DoesNotGenerateTokens_whenUserNotFound(
      Credentials credentials) {
    when(userService.findByEmail(any())).thenReturn(Optional.empty());
    when(userService.findByName(any())).thenReturn(Optional.empty());
    when(userAgentParser.parse(anyString()))
        .thenReturn(getStubUserAgentParserResult());

    when(encoder.matches(eq(TEST_PASSWORD), eq(TEST_PASSWORD))).thenReturn(true);

    Optional<GeneratedToken> tokens = facade
        .requestTokenForUser(credentials, headers);

    Assert.assertNull(tokens.orElse(null), "Tokens should NOT be present");
    verify(encoder, never()).matches(eq(TEST_PASSWORD), eq(TEST_PASSWORD));
  }

  @Test
  public void testRefreshTokenForUser_createsNewToken_whenTokenFound() {
    final String refreshToken = "RT-iurvbkuybvubv";
    final String accessToken = "AT-sdkjfsjhdbfiu43u";
    User user = mock(User.class);

    Token tokenFromRepository = new Token();
    tokenFromRepository.setUser(user);
    tokenFromRepository.setExpired(getStubDateAfterCurrentDate());

    UserAgentParserResult stubUserAgentParserResult = getStubUserAgentParserResult();
    tokenFromRepository.setOs(stubUserAgentParserResult.getOs().toString());
    tokenFromRepository.setBrowser(stubUserAgentParserResult.getBrowser().toString());

    when(userService.findTokenByRefreshToken(eq(refreshToken)))
        .thenReturn(Optional.of(tokenFromRepository));
    when(jwtCodec.buildToken(any(User.class), any(Date.class), anyString()))
        .thenReturn(accessToken);
    when(userAgentParser.parse(anyString()))
        .thenReturn(getStubUserAgentParserResult());
    when(ipParser.parse(any(HttpHeaders.class)))
        .thenReturn(USER_AGENT);

    Optional<GeneratedToken> tokens = facade
        .refreshTokenForUser(refreshToken, headers);

    Assert.assertNotNull(tokens.orElse(null), "Tokens should be present");
    verify(userService).saveToken(eq(tokenFromRepository));
  }

  @Test
  public void testRefreshTokenForUser_doesNotCreateNewToken_whenNoTokenFound() {
    final String refreshToken = "refreshToken";
    when(userService.findTokenByRefreshToken(eq(refreshToken)))
        .thenReturn(Optional.empty());
    Optional<GeneratedToken> tokens = facade
        .refreshTokenForUser(refreshToken, headers);

    Assert.assertNull(tokens.orElse(null), "Tokens should NOT be present");
    verify(userService, never()).saveToken(any());
  }

  /**
   * Creates a current date shifted on the offset in milliseconds.
   *
   * @return Date object
   */
  private Date getStubDateAfterCurrentDate() {
    final int offsetInMillis = 50_000;
    return new Date(System.currentTimeMillis() + offsetInMillis);
  }

  private UserAgentParserResult getStubUserAgentParserResult() {
    return new UserAgentParserResult(
        OS.builder().name("windows").version("10").build(),
        Browser.builder().name("firefox").version("10").build());
  }

}
