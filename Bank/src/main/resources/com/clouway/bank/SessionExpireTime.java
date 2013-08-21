package com.clouway.bank;

import com.google.inject.Inject;
import org.apache.commons.dbcp.PoolableConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class SessionExpireTime implements ExpireTime {
    private final int expireTimeInMin = 3;
    private ConnectionProvider connectionProvider;
    private SQLActionPerformer sqlActionPerformer;

    @Inject
    public SessionExpireTime(ConnectionProvider connectionProvider, SQLActionPerformer sqlActionPerformer) {
        this.connectionProvider = connectionProvider;
        this.sqlActionPerformer = sqlActionPerformer;
    }

    @Override
    public void createExpireTimeFor(final String username) {
        final PoolableConnection connection = connectionProvider.get();
        sqlActionPerformer.executeSQLAction(new SQLAction<Void>(connection) {
            @Override
            public Void execute() throws SQLException {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, expireTimeInMin);
                Date expireTime = calendar.getTime();
                PreparedStatement preparedStatement = connection.prepareStatement("insert into expire_time(username,time) value (?,?)");
                preparedStatement.setString(1, username);
                preparedStatement.setTimestamp(2, new Timestamp(expireTime.getTime()));
                preparedStatement.execute();
                return null;
            }
        });
    }

    @Override
    public void deleteExpireTimeFor(final String username) {
        final PoolableConnection connection = connectionProvider.get();
        sqlActionPerformer.executeSQLAction(new SQLAction<Void>(connection) {
            @Override
            public Void execute() throws SQLException {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from expire_time where username = ?");
                preparedStatement.setString(1, username);
                preparedStatement.execute();
                return null;
            }
        });
    }

    @Override
    public Timestamp getExpireTimeFor(final String username) {
        final PoolableConnection connection = connectionProvider.get();
        return (Timestamp) sqlActionPerformer.executeSQLAction(new SQLAction<Timestamp>(connection) {
            @Override
            public Timestamp execute() throws SQLException {
                Timestamp expiredTime = null;
                PreparedStatement ps = connection.prepareStatement("select time from expire_time where username = ?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                expiredTime = rs.getTimestamp("time");
                return expiredTime;
            }
        });
    }
}
