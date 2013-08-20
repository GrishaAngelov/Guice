package com.clouway.bank;

import org.apache.commons.dbcp.PoolableConnection;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class BankUserRepository implements UserRepository {

    public boolean add(User user) {
        PoolableConnection connection = ConnectionFilter.get();
        Statement statement;
        boolean isSuccessfullyRegistered = false;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT username FROM users");
            if (checkIsUsernameAvailable(resultSet, user.getUsername())) {
                statement.execute("INSERT INTO users VALUES ('" + user.getUsername() + "'," + "'" + user.getPassword() + "')");
                statement.execute("INSERT INTO bank_account VALUES ('" + user.getUsername() + "'," + "'" + new BigDecimal("0") + "')");
                isSuccessfullyRegistered = true;
            } else {
                isSuccessfullyRegistered = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return isSuccessfullyRegistered;
    }


    public boolean hasUser(final User user) {
        final PoolableConnection connection = ConnectionFilter.get();
        return (Boolean) executeSQLAction(new SQLAction<Boolean>(connection) {
            @Override
            public Boolean execute() throws SQLException {
                boolean isRegistered = false;
                ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM users where username=\""+user.getUsername()+"\"");
                while (rs.next()) {
                    if (rs.getString("username").equals(user.getUsername()) && rs.getString("password").equals(user.getPassword())) {
                        isRegistered = true;
                    }
                }
                return isRegistered;
            }
        });
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

    private void closeConnection(PoolableConnection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private <T> T executeSQLAction(Action<T> action) {
        try {
            return action.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                action.finish();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public interface Action<T> {
        T execute() throws SQLException;

        void finish() throws SQLException;
    }

    abstract class SQLAction<T> implements Action {

        private PoolableConnection connection;

        protected SQLAction(PoolableConnection connection) {
            this.connection = connection;
        }

        @Override
        public void finish() throws SQLException {
            connection.close();
        }
    }


}
