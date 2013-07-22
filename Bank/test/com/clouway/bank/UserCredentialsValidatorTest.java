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
        assertTrue(userCredentialsValidator.validate("John", "123456"));
    }

    @Test
    public void validateEmptyUsername() throws Exception {
        assertFalse(userCredentialsValidator.validate("", "123456"));
    }

    @Test
    public void validateEmptyPassword() throws Exception {
        assertFalse(userCredentialsValidator.validate("John", ""));
    }

    @Test
    public void validateEmptyCredentials(){
        assertFalse(userCredentialsValidator.validate("", ""));
    }
}
