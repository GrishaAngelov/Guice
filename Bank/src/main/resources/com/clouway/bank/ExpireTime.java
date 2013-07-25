package com.clouway.bank;

import java.sql.Timestamp;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public interface ExpireTime {
    void createExpireTimeFor(String username);
    void deleteExpireTimeFor(String username);
    Timestamp getExpireTimeFor(String username);
}
