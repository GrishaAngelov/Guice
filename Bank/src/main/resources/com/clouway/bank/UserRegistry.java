package com.clouway.bank;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public interface UserRegistry {
    boolean registerUser(String username, String password);

    boolean isUserRegistered(String username, String password);
}
