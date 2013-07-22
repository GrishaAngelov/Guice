package com.clouway.bank;

import org.apache.commons.dbcp.PoolableConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;

import static junit.framework.Assert.*;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class BankAccountIntegrationTest {
    private BankAccount account;
    private PoolableConnection connection;
    private Statement statement;
    private final BigDecimal initAmount = new BigDecimal("50.00");

    @Rule
    public ConnectionRule connectionRule = new ConnectionRule();

    @Before
    public void setUp() throws SQLException {
        account = new BankAccount(99999999,new ConnectionProvider());
        connection = connectionRule.get();
        statement = connection.createStatement();
        statement.execute("create table if not exists bank_account (username varchar(20) not null primary key, amount decimal (10,2))");
        statement.execute("insert into bank_account values (\"john\", 50.00)");
    }

    @After
    public void tearDown() throws SQLException {
        statement.execute("drop table bank_account");
        statement.close();
        connection.close();
    }

    @Test
    public void getBalanceForExistingUser() {
        BigDecimal balance = account.getBalance("john");
        assertEquals(initAmount, balance);
    }

    @Test(expected = NoSuchUserException.class)
    public void getBalanceForNotExistingUser() {
        account.getBalance("pesho");
    }

    @Test(expected = NoSuchUserException.class)
    public void getBalanceForEmptyUser() {
        account.getBalance("");
    }

    @Test
    public void depositAmountHappyPath() throws Exception {
        boolean isAmountDeposited = account.deposit("20", "john");

        assertTrue(isAmountDeposited);
        assertEquals(new BigDecimal("70.00"), account.getBalance("john"));
    }

    @Test
    public void depositAmountWithDotDelimiter(){
        boolean isAmountDeposited = account.deposit("20.00","john");

        assertTrue(isAmountDeposited);
        assertEquals(new BigDecimal("70.00"), account.getBalance("john"));
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void depositAmountWithCommaDelimiter(){
       account.deposit("20,00","john");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void depositEmptyAmount() throws Exception {
        account.deposit("", "john");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void depositNegativeAmount() {
        account.deposit("-20", "john");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void depositZeroAmount() {
        account.deposit("0", "john");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void depositTooBigAmount() {
        account.deposit("9999999999999999999999", "john");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void depositStringAmount() {
        account.deposit("asd", "john");
    }

    @Test(expected = NoSuchUserException.class)
    public void depositAmountToEmptyUser() throws Exception {
        account.deposit("20", "");
    }

    @Test(expected = NoSuchUserException.class)
    public void depositAmountToNonexistentUser() throws Exception {
        account.deposit("20", "pesho");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void depositEmptyAmountToEmptyUser() {
        account.deposit("", "");
    }

    @Test
    public void depositMaxLimit() {
        account.deposit("99999949", "john");
        assertEquals("99999999.00",account.getBalance("john").toString());
    }

    @Test
    public void depositMoreThanMaxLimit(){
        account.deposit("99999949", "john");
        boolean isSuccessful = account.deposit("1","john");
        assertFalse(isSuccessful);
    }

    @Test
    public void withdrawAmountHappyPath() {
        boolean isSuccessful = account.withdraw("10", "john");

        assertTrue(isSuccessful);
        assertEquals(new BigDecimal("40.00"), account.getBalance("john"));
    }

    @Test
    public void withdrawAll() {
        boolean isSuccessful = account.withdraw("50", "john");

        assertTrue(isSuccessful);
        assertEquals(new BigDecimal("0.00"), account.getBalance("john"));
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void withdrawZeroAmount() {
        account.withdraw("0", "John");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void withdrawNegativeAmount() {
        account.withdraw("-10", "john");
    }

    @Test
    public void withdrawMoreThanAvailable() {
        boolean isSuccessful = account.withdraw("100", "john");

        assertFalse(isSuccessful);
        assertEquals(initAmount, account.getBalance("john"));
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void withdrawStringAmount() {
        account.withdraw("asd", "john");
    }

    @Test(expected = NoSuchUserException.class)
    public void withdrawAmountFromNonexistentUser() {
        account.withdraw("10", "pesho");
    }

    @Test(expected = NoSuchUserException.class)
    public void withdrawAmountFromEmptyUser() {
        account.withdraw("10", "");
    }

    @Test(expected = IncorrectAmountValueException.class)
    public void withdrawEmptyAmountFromEmptyUser() {
        account.withdraw("", "");
    }

}
