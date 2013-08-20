package com.clouway.bank;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class UserCredentialsValidator implements CredentialsValidator {

  public boolean isValid(User user) {
    return !user.getUsername().equals("") && !user.getPassword().equals("");
  }
}
