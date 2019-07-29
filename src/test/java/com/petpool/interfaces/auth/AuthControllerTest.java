package com.petpool.interfaces.auth;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.petpool.interfaces.auth.facade.AuthFacade;
import com.petpool.interfaces.auth.facade.AuthFacade.Credentials;
import java.util.Map;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AuthControllerTest {

  private static final String TEST_NAME = "testName";
  private static final String TEST_EMAIL = "test@email.test";
  private static final String TEST_PASSWORD = "secretPassword";
  private static final String EMPTY = "";
  private static final String USER_AGENT = "testUserAgent";

  @Mock private AuthFacade facade;
  @InjectMocks private AuthController controller;

  @BeforeClass
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void tearDown() {
    reset(facade);
  }

  @DataProvider(name = "validCredentials")
  public Object[][] getValidCredentials() {
    return new Object[][]{
        {new Credentials(TEST_NAME, TEST_EMAIL, TEST_PASSWORD)},
        {new Credentials(EMPTY, TEST_EMAIL, TEST_PASSWORD)},
        {new Credentials(TEST_NAME, EMPTY, TEST_PASSWORD)}
    };
  }

  @Test(dataProvider = "validCredentials")
  public void testGetToken_returnsTokens_whenCredentialsValid(Credentials credentials) {
    final Map<String, String> tokens = ImmutableMap.of("token", "testToken");
    when(facade.requestTokenForUser(eq(credentials), eq(USER_AGENT)))
        .thenReturn(Optional.of(tokens));
    ResponseEntity<Map<String, String>> response = controller.getToken(credentials, USER_AGENT);
    Assert.assertEquals(response.getStatusCode(), HttpStatus.OK, "Status code should be OK");
    Assert.assertEquals(response.getBody(), tokens, "Correct tokens map should be returned");
  }

  @DataProvider(name = "invalidCredentials")
  public Object[][] getInvalidCredentials() {
    return new Object[][]{
        {new Credentials(EMPTY, EMPTY, EMPTY)},
        {new Credentials(EMPTY, TEST_EMAIL, EMPTY)},
        {new Credentials(TEST_NAME, EMPTY, EMPTY)},
        {new Credentials(TEST_NAME, TEST_PASSWORD, EMPTY)},
    };
  }

  @Test(dataProvider = "invalidCredentials")
  public void testGetToken_returnsError_whenCredentialsValid(Credentials credentials) {
    ResponseEntity<Map<String, String>> response = controller.getToken(credentials, USER_AGENT);
    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus.UNAUTHORIZED,
        "Status code should be 401");
    verify(facade, never()).requestTokenForUser(Mockito.any(Credentials.class), eq(USER_AGENT));
  }

}
