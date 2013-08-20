package com.clouway.bank;

/**
 * User repository responsible for storing and retrieving users
 *
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public interface UserRepository {

    /**
     * @param user
     * @return true if user is registered
     */
    boolean add(User user);

    /**
     * @param user
     * @return
     */
    boolean hasUser(User user);
}
