package com.petpool.interfaces.auth;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.petpool.application.util.response.Response;
import com.petpool.interfaces.auth.facade.AuthFacade;
import com.petpool.interfaces.auth.facade.AuthFacade.Credentials;
import com.petpool.interfaces.auth.facade.GeneratedToken;
import java.util.Optional;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
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
  private static final HttpHeaders headers = new HttpHeaders();


  @Mock private AuthFacade facade;
  @InjectMocks private AuthController controller;

  @BeforeClass
  public void init() {
    MockitoAnnotations.initMocks(this);
    headers.set("User-Agent",USER_AGENT);
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
    final GeneratedToken tokens = new GeneratedToken("accesstoken","refreshtoken", System.currentTimeMillis());
    when(facade.requestTokenForUser(eq(credentials), eq(headers)))
        .thenReturn(Optional.of(tokens));
    ResponseEntity<Response> response = controller.getToken(credentials, headers);
    Assert.assertEquals(response.getStatusCode(), HttpStatus.OK, "Status code should be OK");
    Assert.assertEquals(response.getBody().getPayload(), tokens, "Correct tokens map should be returned");
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
    ResponseEntity<Response> response = controller.getToken(credentials, headers);
    Assert.assertEquals(
        response.getStatusCode(),
        HttpStatus.OK,
        "Status code should be 200");

    Assert.assertTrue(response.getBody().isErrorPresent(), "isErrorPresent should be true");
    verify(facade, never()).requestTokenForUser(Mockito.any(Credentials.class), eq(headers));
  }

  @Test
  public void testCheckUnique_returnsError_whenParametersIsEmpty() {
    ResponseEntity<Response> response = controller.checkUnique(" ", "");
    Assert.assertEquals(response.getStatusCode(), HttpStatus.OK, "Status code should be OK");
    Assert.assertTrue(response.getBody().isErrorPresent(), "isErrorPresent should be true");
    Assert.assertNotNull(response.getBody().getError(), "Error code shouldn't be empty");
  }

  @Test
  public void testCheckUnique_returnsError_whenParametersIsExisted() {
    ResponseEntity<Response> response = controller.checkUnique(TEST_NAME, TEST_EMAIL);
    Assert.assertEquals(response.getStatusCode(), HttpStatus.OK, "Status code should be OK");
    Assert.assertEquals(response.getBody().getError(), null, "Error should be null");
    Assert.assertNull(response.getBody().getError(), "Error should be empty");
    Assert.assertNotNull(response.getBody().getPayload(), "Response shouldn't be empty");
  }
}
