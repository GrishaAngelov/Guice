package com.clouway.bank;

import java.math.BigDecimal;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public interface Account {
  boolean deposit(String depositAmount, String username);

  boolean withdraw(String withdrawAmount, String username);

  BigDecimal getBalance(String username);
}
