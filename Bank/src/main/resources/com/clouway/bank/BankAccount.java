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
    public BankAccount(@Named("limit") Double limitAmountValue, Provider<PoolableConnection> connectionProvider) {
        LIMIT_AMOUNT_VALUE = limitAmountValue;
        this.connectionProvider = connectionProvider;
    }

    @Override
    public boolean deposit(BigDecimal depositAmount, String username) {
        PoolableConnection poolableConnection = null;
        boolean isSuccessful = false;
        try {
            poolableConnection = connectionProvider.get();
            checkIsUserExist(username);
            PreparedStatement statement = poolableConnection.prepareStatement("update bank_account set amount = amount + ? where username = ?");

            if (shouldDepositAmount(depositAmount, username)) {
                statement.setString(1, depositAmount.toString());
                statement.setString(2, username);
                statement.execute();
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            throw new IncorrectAmountValueException();
        } finally {
            closeConnection(poolableConnection);
        }
        return isSuccessful;
    }

    @Override
    public boolean withdraw(BigDecimal withdrawAmount, String username) {
        PoolableConnection poolableConnection = null;
        boolean isSuccessful = false;
        try {
            poolableConnection = connectionProvider.get();
            Statement statement = poolableConnection.createStatement();
            BigDecimal oldAmount = getCurrentAmount(username);
            checkIsUserExist(username);
            if ((withdrawAmount.doubleValue() <= oldAmount.doubleValue()) && (withdrawAmount.doubleValue() > 0)) {
                statement.execute("UPDATE bank_account SET amount = amount-" + withdrawAmount + " WHERE username='" + username + "'");
                isSuccessful = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(poolableConnection);
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
                throw new UserNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(poolableConnection);
        }
        return new BigDecimal(amount);
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
            closeConnection(connection);
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
                throw new UserNotFoundException();
            }
        } finally {
            closeConnection(connection);
            statement.close();
        }
    }

    private boolean shouldDepositAmount(BigDecimal newAmount, String username) throws SQLException {
        boolean shouldDeposit = false;
        String amountFormatRegEx = "^[0-9]+$|^[0-9]+[.][0-9]{2}$";
        boolean isDepositAmountCorrect = newAmount.toString().matches(amountFormatRegEx);
        if (isDepositAmountCorrect) {
            PoolableConnection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select amount from bank_account where username='" + username + "'");
            resultSet.next();
            BigDecimal oldAmount = new BigDecimal(resultSet.getString("amount"));
            shouldDeposit = newAmount.add(oldAmount).doubleValue() <= LIMIT_AMOUNT_VALUE;
        }
        return shouldDeposit;
    }

    private void closeConnection(PoolableConnection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
