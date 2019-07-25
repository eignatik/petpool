package com.petpool.interfaces.auth.facade;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.Token;
import com.petpool.domain.model.user.User;
import com.petpool.domain.model.user.UserType;
import com.petpool.domain.service.UserService;
import com.petpool.interfaces.auth.facade.AuthFacade.Credentials;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

  @Mock private UserService userService;
  @Mock private PasswordEncoder encoder;
  @InjectMocks private AuthFacadeImpl facade;

  @BeforeClass
  public void init() {
    MockitoAnnotations.initMocks(this);
    facade.setPasswordEncoder(encoder);
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

    Optional<Map<String, String>> tokens = facade
        .requestTokenForUser(credentials, USER_AGENT);

    Assert.assertNotNull(tokens.orElse(null), "Tokens should be present");
  }

  @Test(dataProvider = "credentials")
  public void testRequestTokenForUser_DoesNotGenerateTokens_whenUserNotFound(
      Credentials credentials) {
    when(userService.findByEmail(any())).thenReturn(Optional.empty());
    when(userService.findByName(any())).thenReturn(Optional.empty());

    when(encoder.matches(eq(TEST_PASSWORD), eq(TEST_PASSWORD))).thenReturn(true);

    Optional<Map<String, String>> tokens = facade
        .requestTokenForUser(credentials, USER_AGENT);

    Assert.assertNull(tokens.orElse(null), "Tokens should NOT be present");
    verify(encoder, never()).matches(eq(TEST_PASSWORD), eq(TEST_PASSWORD));
  }

  @Test
  public void testRefreshTokenForUser_createsNewToken_whenTokenFound() {
    final String refreshToken = "refreshToken";
    User user = mock(User.class);
    Token tokenFromRepository = new Token();
    tokenFromRepository.setUser(user);
    when(userService.findTokenByRefreshToken(eq(refreshToken)))
        .thenReturn(Optional.of(tokenFromRepository));
    Optional<Map<String, String>> tokens = facade
        .refreshTokenForUser(refreshToken, USER_AGENT);

    Assert.assertNotNull(tokens.orElse(null), "Tokens should be present");
    verify(userService).saveToken(eq(tokenFromRepository));
  }

  @Test
  public void testRefreshTokenForUser_doesNotCreateNewToken_whenNoTokenFound() {
    final String refreshToken = "refreshToken";
    when(userService.findTokenByRefreshToken(eq(refreshToken)))
        .thenReturn(Optional.empty());
    Optional<Map<String, String>> tokens = facade
        .refreshTokenForUser(refreshToken, USER_AGENT);

    Assert.assertNull(tokens.orElse(null), "Tokens should NOT be present");
    verify(userService, never()).saveToken(any());
  }

}
