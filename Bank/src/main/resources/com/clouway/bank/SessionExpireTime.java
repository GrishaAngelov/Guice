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

    @Inject
    public SessionExpireTime(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public void createExpireTimeFor(String username) {
        PoolableConnection connection = connectionProvider.get();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expireTimeInMin);
        Date expireTime = calendar.getTime();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into expire_time(username,time) value (?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setTimestamp(2, new Timestamp(expireTime.getTime()));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteExpireTimeFor(String username) {
        PoolableConnection connection = connectionProvider.get();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from expire_time where username = ?");
            preparedStatement.setString(1, username);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Timestamp getExpireTimeFor(String username) {
        PoolableConnection connection = connectionProvider.get();
        Timestamp expiredTime = null;
        try {
            PreparedStatement ps = connection.prepareStatement("select time from expire_time where username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            expiredTime = rs.getTimestamp("time");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return expiredTime;
    }
}
