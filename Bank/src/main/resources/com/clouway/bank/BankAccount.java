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
    private SQLActionPerformer sqlActionPerformer;

    @Inject
    public BankAccount(@Named("limit") Double limitAmountValue, Provider<PoolableConnection> connectionProvider, SQLActionPerformer sqlActionPerformer) {
        LIMIT_AMOUNT_VALUE = limitAmountValue;
        this.connectionProvider = connectionProvider;
        this.sqlActionPerformer = sqlActionPerformer;
    }

    @Override
    public boolean deposit(final BigDecimal depositAmount, final String username) {
        final PoolableConnection poolableConnection = connectionProvider.get();
        return (Boolean) sqlActionPerformer.executeSQLAction(new SQLAction<Boolean>(poolableConnection) {
            @Override
            public Boolean execute() throws SQLException {
                boolean isSuccessful = false;
                checkIsUserExist(username);
                PreparedStatement statement = poolableConnection.prepareStatement("update bank_account set amount = amount + ? where username = ?");

                if (shouldDepositAmount(depositAmount, username)) {
                    statement.setString(1, depositAmount.toString());
                    statement.setString(2, username);
                    statement.execute();
                    isSuccessful = true;
                }
                return isSuccessful;
            }
        });
    }

    @Override
    public boolean withdraw(final BigDecimal withdrawAmount, final String username) {
        final PoolableConnection connection = ConnectionFilter.get();
        return (Boolean) sqlActionPerformer.executeSQLAction(new SQLAction<Boolean>(connection) {
            @Override
            public Boolean execute() throws SQLException {
                boolean isSuccessful = false;
                Statement statement = connection.createStatement();
                BigDecimal currentAmount = getCurrentAmount(username);
                checkIsUserExist(username);
                if (hasSufficientMoneyInAccount(currentAmount, withdrawAmount)) {
                    statement.execute("UPDATE bank_account SET amount = amount-" + withdrawAmount + " WHERE username='" + username + "'");
                    isSuccessful = true;
                }
                return isSuccessful;
            }
        });
    }

    @Override
    public BigDecimal getBalance(final String username) {
        final PoolableConnection connection = ConnectionFilter.get();
        final String[] amount = {"0"};
        return (BigDecimal) sqlActionPerformer.executeSQLAction(new SQLAction<BigDecimal>(connection) {
            @Override
            public BigDecimal execute() throws SQLException {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT amount FROM bank_account WHERE username='" + username + "'");
                while (resultSet.next()) {
                    amount[0] = resultSet.getString("amount");
                }
                if (!resultSet.first()) {
                    throw new UserNotFoundException();
                }
                return new BigDecimal(amount[0]);
            }
        });
    }


    private BigDecimal getCurrentAmount(final String username) throws SQLException {
        final PoolableConnection connection = ConnectionFilter.get();
        return (BigDecimal) sqlActionPerformer.executeSQLAction(new SQLAction<BigDecimal>(connection) {
            @Override
            public BigDecimal execute() throws SQLException {
                Statement statement = connection.createStatement();
                BigDecimal currentAmount = null;
                ResultSet rs = statement.executeQuery("SELECT amount FROM bank_account WHERE username='" + username + "'");
                while (rs.next()) {
                    currentAmount = new BigDecimal(rs.getString("amount"));
                }
                return currentAmount;
            }
        });
    }

    private void checkIsUserExist(final String username) throws SQLException {
        final PoolableConnection connection = ConnectionFilter.get();
        final Statement[] statement = {null};

        sqlActionPerformer.executeSQLAction(new SQLAction<Void>(connection) {
            @Override
            public Void execute() throws SQLException {
                statement[0] = connection.createStatement();
                ResultSet resultSet = statement[0].executeQuery("select amount from bank_account where username=\"" + username + "\"");
                if (!resultSet.first() || username.equals("")) {
                    throw new UserNotFoundException();
                }
                return null;
            }
        });
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

    private boolean hasSufficientMoneyInAccount(BigDecimal currentAmount, BigDecimal withdrawAmount) {
        return (withdrawAmount.doubleValue() <= currentAmount.doubleValue()) && (withdrawAmount.doubleValue() > 0);
    }
}
