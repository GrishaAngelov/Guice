package com.clouway.bank;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public interface CredentialsValidator {
    boolean validate(String username, String password);
}
