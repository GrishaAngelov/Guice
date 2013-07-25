package com.clouway.bank;

import org.apache.commons.dbcp.PoolableConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */

public class LoggedUsersCounter {
    private static ConnectionProvider connectionProvider = new ConnectionProvider();
    public static int getCount() {
        PoolableConnection connection = connectionProvider.get();
        int count = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("select count(*) as counted from expire_time");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                count = rs.getInt("counted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }
}


