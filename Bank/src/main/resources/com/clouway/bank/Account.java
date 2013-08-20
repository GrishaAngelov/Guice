package com.clouway.bank;

import java.math.BigDecimal;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public interface Account {
  boolean deposit(BigDecimal depositAmount, String username);

  boolean withdraw(BigDecimal withdrawAmount, String username);

  BigDecimal getBalance(String username);
}
