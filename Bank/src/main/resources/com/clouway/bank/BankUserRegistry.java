package com.clouway.bank;

import org.apache.commons.dbcp.PoolableConnection;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class BankUserRegistry implements UserRegistry {

    public boolean registerUser(String username, String password) {
        PoolableConnection connection = ConnectionFilter.get();
        Statement statement = null;
        boolean isSuccessfullyRegistered = false;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT username FROM users");
            if (checkIsUsernameAvailable(resultSet, username)) {
                statement.execute("INSERT INTO users VALUES ('" + username + "'," + "'" + password + "')");
                statement.execute("INSERT INTO bank_account VALUES ('" + username + "'," + "'" + new BigDecimal("0") + "')");
                isSuccessfullyRegistered = true;
            } else {
                isSuccessfullyRegistered = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return isSuccessfullyRegistered;
    }


    public boolean isUserRegistered(String username, String password) {
        PoolableConnection connection = ConnectionFilter.get();
        boolean isRegistered = false;
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM users");
            while (rs.next()) {
                if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
                    isRegistered = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isRegistered;
    }

    private boolean checkIsUsernameAvailable(ResultSet resultSet, String username) throws SQLException {
        boolean isAvailable = true;
        while (resultSet.next()) {
            if (resultSet.getString("username").equals(username)) {
                isAvailable = false;
                break;
            }
        }
        return isAvailable;
    }
}
