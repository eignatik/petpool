package com.petpool.interfaces.validation;

import static org.mockito.Mockito.reset;
import com.petpool.application.util.response.Response;
import com.petpool.interfaces.validation.facade.UserAuthFacade;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserAuthControllerTest {

    private static final String TEST_NAME = "testName";
    private static final String TEST_EMAIL = "test@email.test";

    @Mock
    private UserAuthFacade facade;

    @InjectMocks
    private UserAuthController controller;

    @BeforeClass
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void tearDown() {
        reset(facade);
    }

    @Test
    public void testCheckUniqueUserName_returnsError_whenParametersIsEmpty() {
        ResponseEntity<Response> response = controller.checkUniqueByLogin(" ");
        returnsErrorWhenParametersIsEmpty(response);
    }

    @Test
    public void testCheckUniqueEmail_returnsError_whenParametersIsEmpty() {
        ResponseEntity<Response> response = controller.checkUniqueByEmail(" ");
        returnsErrorWhenParametersIsEmpty(response);
    }

    @Test
    public void testCheckUniqueUserName_returnsError_whenParametersIsExisted() {
        ResponseEntity<Response> response = controller.checkUniqueByLogin(TEST_NAME);
        returnsErrorWhenParametersIsExisted(response);
    }

    @Test
    public void testCheckUniqueEmail_returnsError_whenParametersIsExisted() {
        ResponseEntity<Response> response = controller.checkUniqueByEmail(TEST_EMAIL);
        returnsErrorWhenParametersIsExisted(response);
    }

    private void returnsErrorWhenParametersIsEmpty(ResponseEntity<Response> response) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK, "Status code should be OK");
        Assert.assertTrue(response.getBody().isErrorPresent(), "isErrorPresent should be true");
    }

    private void returnsErrorWhenParametersIsExisted(ResponseEntity<Response> response) {
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK, "Status code should be OK");
        Assert.assertNotNull(response.getBody().getPayload(), "Response shouldn't be empty");
    }

}
