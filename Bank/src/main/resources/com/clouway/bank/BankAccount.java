package com.clouway.bank;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.dbcp.PoolableConnection;

import javax.inject.Provider;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class BankAccount implements Account {
    private static double LIMIT_AMOUNT_VALUE = 0;
    private Provider<PoolableConnection> connectionProvider;

    @Inject
    public BankAccount(@Named("limit") double limitAmountValue, Provider<PoolableConnection> connectionProvider) {
        LIMIT_AMOUNT_VALUE = limitAmountValue;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public boolean deposit(String depositAmount, String username) {
        checkAmountValue(depositAmount);
        PoolableConnection poolableConnection = null;
        boolean isSuccessful = false;
        try {
            poolableConnection = connectionProvider.get();
            checkIsUserExist(username);
            PreparedStatement statement = poolableConnection.prepareStatement("update bank_account set amount = amount + ? where username = ?");

            if (shouldDepositAmount(depositAmount, username)) {
                BigDecimal amountValue = new BigDecimal(depositAmount);
                statement.setString(1, amountValue.toString());
                statement.setString(2, username);
                statement.execute();
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                poolableConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccessful;
    }

    @Override
    public boolean withdraw(String withdrawAmount, String username) {
        checkAmountValue(withdrawAmount);
        PoolableConnection poolableConnection = null;
        boolean isSuccessful = false;
        try {
            poolableConnection = connectionProvider.get();
            Statement statement = poolableConnection.createStatement();
            BigDecimal amountValue = new BigDecimal(withdrawAmount);
            BigDecimal oldAmount = getCurrentAmount(username);
            checkIsUserExist(username);
            if ((amountValue.doubleValue() <= oldAmount.doubleValue()) && (amountValue.doubleValue() > 0)) {
                statement.execute("UPDATE bank_account SET amount = amount-" + amountValue + " WHERE username='" + username + "'");
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                poolableConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccessful;
    }


    @Override
    public BigDecimal getBalance(String username) {
        PoolableConnection poolableConnection = null;
        String amount = "0";
        try {
            poolableConnection = connectionProvider.get();
            Statement statement = poolableConnection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT amount FROM bank_account WHERE username='" + username + "'");
            while (resultSet.next()) {
                amount = resultSet.getString("amount");
            }
            if (!resultSet.first()) {
                throw new NoSuchUserException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                poolableConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return new BigDecimal(amount);
    }


    private void checkAmountValue(String amount) {
        if (amount.equals("")) {
            throw new IncorrectAmountValueException();
        } else {
            try {
                if (Double.parseDouble(amount) < 0 || amount.equals("0") || Double.parseDouble(amount) > LIMIT_AMOUNT_VALUE) {
                    throw new IncorrectAmountValueException();
                }
            } catch (NumberFormatException e) {
                throw new IncorrectAmountValueException();
            }
        }
    }

    private BigDecimal getCurrentAmount(String username) throws SQLException {
        PoolableConnection connection = connectionProvider.get();
        Statement statement = connection.createStatement();
        BigDecimal oldAmount = null;
        try {
            ResultSet rs = statement.executeQuery("SELECT amount FROM bank_account WHERE username='" + username + "'");
            while (rs.next()) {
                oldAmount = new BigDecimal(rs.getString("amount"));
            }
        } finally {
            connection.close();
            statement.close();
        }
        return oldAmount;
    }

    private void checkIsUserExist(String username) throws SQLException {
        PoolableConnection connection = null;
        Statement statement = null;
        try {
            connection = connectionProvider.get();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select amount from bank_account where username=\"" + username + "\"");
            if (!resultSet.first() || username.equals("")) {
                throw new NoSuchUserException();
            }
        } finally {
            statement.close();
            connection.close();
        }
    }

    private boolean shouldDepositAmount(String depositAmount, String username) throws SQLException {
        boolean shouldDeposit = false;
        boolean isDepositAmountCorrect = depositAmount.matches("^[0-9]+$|^[0-9]+[.][0-9]{2}$");
        if (isDepositAmountCorrect) {
            PoolableConnection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select amount from bank_account where username='" + username + "'");
            resultSet.next();
            BigDecimal newAmount = new BigDecimal(depositAmount);
            BigDecimal oldAmount = new BigDecimal(resultSet.getString("amount"));
            shouldDeposit = newAmount.add(oldAmount).doubleValue() <= LIMIT_AMOUNT_VALUE;
        }
        return shouldDeposit;
    }
}
