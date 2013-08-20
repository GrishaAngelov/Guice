package com.clouway.bank;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class UserCredentialsValidatorTest {
    private UserCredentialsValidator userCredentialsValidator;

    @Before
    public void setUp() throws Exception {
        userCredentialsValidator = new UserCredentialsValidator();
    }

    @Test
    public void validateCredentialsHappyPath() throws Exception {
        assertTrue(userCredentialsValidator.isValid(new User("John", "123456")));
    }

    @Test
    public void validateEmptyUsername() throws Exception {
        assertFalse(userCredentialsValidator.isValid(new User("", "123456")));
    }

    @Test
    public void validateEmptyPassword() throws Exception {
        assertFalse(userCredentialsValidator.isValid(new User("John", "")));
    }

    @Test
    public void validateEmptyCredentials(){
        assertFalse(userCredentialsValidator.isValid(new User("", "")));
    }
}
