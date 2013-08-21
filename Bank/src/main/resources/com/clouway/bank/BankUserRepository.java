package com.clouway.bank;

import com.google.inject.Inject;
import org.apache.commons.dbcp.PoolableConnection;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class BankUserRepository implements UserRepository {

    private SQLActionPerformer sqlActionPerformer;

    @Inject
    public BankUserRepository(SQLActionPerformer sqlActionPerformer) {
        this.sqlActionPerformer = sqlActionPerformer;
    }

    public boolean add(final User user) {
        final PoolableConnection connection = ConnectionFilter.get();
        return (Boolean) sqlActionPerformer.executeSQLAction(new SQLAction<Boolean>(connection) {
            @Override
            public Boolean execute() throws SQLException {
                Statement statement;
                boolean isSuccessfullyRegistered;
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT username FROM users where username=\"" + user.getUsername() + "\"");
                if (checkIsUsernameAvailable(resultSet, user.getUsername())) {
                    statement.execute("INSERT INTO users VALUES ('" + user.getUsername() + "'," + "'" + user.getPassword() + "')");
                    statement.execute("INSERT INTO bank_account VALUES ('" + user.getUsername() + "'," + "'" + new BigDecimal("0").toString() + "')");
                    isSuccessfullyRegistered = true;
                } else {
                    isSuccessfullyRegistered = false;
                }
                return isSuccessfullyRegistered;
            }
        });
    }

    public boolean hasUser(final User user) {
        final PoolableConnection connection = ConnectionFilter.get();
        return (Boolean) sqlActionPerformer.executeSQLAction(new SQLAction<Boolean>(connection) {
            @Override
            public Boolean execute() throws SQLException {
                boolean isRegistered = false;
                ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM users where username=\"" + user.getUsername() + "\"");
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

}
