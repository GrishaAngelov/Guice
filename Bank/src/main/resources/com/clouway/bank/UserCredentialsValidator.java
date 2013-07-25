package com.clouway.bank;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class UserCredentialsValidator implements CredentialsValidator {

  public boolean isValid(String username, String password) {
    boolean isValid = false;
    if (!username.equals("") && !password.equals("")) {
      isValid = true;
    }
    return isValid;
  }
}
