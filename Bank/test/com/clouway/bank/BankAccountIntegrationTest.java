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
        account = new BankAccount(99999999.0,new ConnectionProvider(),new SQLActionPerformer());
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

    @Test(expected = UserNotFoundException.class)
    public void getBalanceForNotExistingUser() {
        account.getBalance("pesho");
    }

    @Test(expected = UserNotFoundException.class)
    public void getBalanceForEmptyUser() {
        account.getBalance("");
    }

    @Test
    public void depositAmountHappyPath() throws Exception {
        boolean isAmountDeposited = account.deposit(new BigDecimal("20"), "john");

        assertTrue(isAmountDeposited);
        assertEquals(new BigDecimal("70.00"), account.getBalance("john"));
    }

    @Test
    public void depositAmountWithDotDelimiter(){
        boolean isAmountDeposited = account.deposit(new BigDecimal("20.00"),"john");

        assertTrue(isAmountDeposited);
        assertEquals(new BigDecimal("70.00"), account.getBalance("john"));
    }

    @Test(expected = NumberFormatException.class)
    public void depositAmountWithCommaDelimiter(){
       account.deposit(new BigDecimal("20,00"),"john");
    }

    @Test(expected = NumberFormatException.class)
    public void depositEmptyAmount() throws Exception {
        account.deposit(new BigDecimal(""), "john");
    }

    @Test(expected = NumberFormatException.class)
    public void depositStringAmount() {
        account.deposit(new BigDecimal("asd"), "john");
    }

    @Test(expected = UserNotFoundException.class)
    public void depositAmountToEmptyUser() throws Exception {
        account.deposit(new BigDecimal("20"), "");
    }

    @Test(expected = UserNotFoundException.class)
    public void depositAmountToNonexistentUser() throws Exception {
        account.deposit(new BigDecimal("20"), "pesho");
    }

    @Test(expected = NumberFormatException.class)
    public void depositEmptyAmountToEmptyUser() {
        account.deposit(new BigDecimal(""), "");
    }

    @Test
    public void depositMaxLimit() {
        account.deposit(new BigDecimal("99999949"), "john");
        assertEquals("99999999.00",account.getBalance("john").toString());
    }

    @Test
    public void depositMoreThanMaxLimit(){
        account.deposit(new BigDecimal("99999949"), "john");
        boolean isSuccessful = account.deposit(new BigDecimal("1"),"john");
        assertFalse(isSuccessful);
    }

    @Test
    public void withdrawAmountHappyPath() {
        boolean isSuccessful = account.withdraw(new BigDecimal("10"), "john");

        assertTrue(isSuccessful);
        assertEquals(new BigDecimal("40.00"), account.getBalance("john"));
    }

    @Test
    public void withdrawAll() {
        boolean isSuccessful = account.withdraw(new BigDecimal("50"), "john");

        assertTrue(isSuccessful);
        assertEquals(new BigDecimal("0.00"), account.getBalance("john"));
    }

    @Test
    public void withdrawMoreThanAvailable() {
        boolean isSuccessful = account.withdraw(new BigDecimal("100"), "john");

        assertFalse(isSuccessful);
        assertEquals(initAmount, account.getBalance("john"));
    }

    @Test(expected = NumberFormatException.class)
    public void withdrawStringAmount() {
        account.withdraw(new BigDecimal("asd"), "john");
    }

    @Test(expected = UserNotFoundException.class)
    public void withdrawAmountFromNonexistentUser() {
        account.withdraw(new BigDecimal("10"), "pesho");
    }

    @Test(expected = UserNotFoundException.class)
    public void withdrawAmountFromEmptyUser() {
        account.withdraw(new BigDecimal("10"), "");
    }

    @Test(expected = NumberFormatException.class)
    public void withdrawEmptyAmountFromEmptyUser() {
        account.withdraw(new BigDecimal(""), "");
    }

}
