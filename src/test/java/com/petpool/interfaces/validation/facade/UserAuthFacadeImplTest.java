package com.petpool.interfaces.validation.facade;

import com.petpool.domain.model.user.Role;
import com.petpool.domain.model.user.User;
import com.petpool.domain.service.UserService;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class UserAuthFacadeImplTest {
    private static final String TEST_NAME = "testName";
    private static final String TEST_EMAIL = "hello@test.test";
    private static final String TEST_PASSWORD_HASH = "hashPassword";

    @Mock
    private UserService userService;

    @InjectMocks
    private UserAuthFacadeImpl userAuthFacade;

    @BeforeClass
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterMethod
    public void tearDown() {
        reset(userService);
    }

    @Test
    public void testCheckUniquenessForUser_returnsError_whenNoUserNameFound() {
        when(userService.findByName(TEST_NAME)).thenReturn(Optional.empty());
        Assert.assertTrue(userAuthFacade.isUniqueByUserName(TEST_NAME), "NickName must be unique");
    }

    @Test
    public void testCheckUniquenessForUser_returnsError_whenNoEmailFound() {
        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        Assert.assertTrue(userAuthFacade.isUniqueByEmail(TEST_EMAIL), "Email must be unique");
    }

    @Test
    public void testCheckNoUniquenessForUser_returnsError_whenNoUserNameFound() {
        User user = createFakeUserWithUserNameAndEmail();

        when(userService.findByName(TEST_NAME)).thenReturn(Optional.of(user));
        Assert.assertFalse(userAuthFacade.isUniqueByUserName(TEST_NAME), "NickName is already taken");
    }

    @Test
    public void testCheckNoUniquenessForUser_returnsError_whenNoEmailFound() {
        User user = createFakeUserWithUserNameAndEmail();

        when(userService.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        Assert.assertFalse(userAuthFacade.isUniqueByEmail(TEST_EMAIL), "Email is already taken");
    }

    private User createFakeUserWithUserNameAndEmail() {
        return new User(TEST_NAME, TEST_PASSWORD_HASH, TEST_EMAIL, mock(Date.class), Set.of(mock(Role.class)));
    }
}
