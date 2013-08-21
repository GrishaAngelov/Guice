package com.clouway.bank;

import org.apache.commons.dbcp.PoolableConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.*;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class SessionExpireTimeTest {
    private ExpireTime expireTime;
    private PoolableConnection connection;
    private Statement statement;
    private final String username = "pesho";

    @Rule
    public ConnectionProviderRule rule = new ConnectionProviderRule();

    @Before
    public void setUp() throws Exception {
        ConnectionProvider connectionProvider = rule.get();
        SQLActionPerformer sqlActionPerformer = new SQLActionPerformer();
        expireTime = new SessionExpireTime(connectionProvider,sqlActionPerformer);
        connection = connectionProvider.get();
        statement = connection.createStatement();

        statement.execute("create table expire_time(username varchar(100) not null, time timestamp not null,primary key(username))");
    }

    @After
    public void tearDown() throws Exception {
        statement.execute("drop table expire_time");
        statement.close();
        connection.close();
    }

    @Test
    public void createExpireTimeForUsername() throws Exception {
        expireTime.createExpireTimeFor(username);

        ResultSet resultSet = statement.executeQuery("select username from expire_time");
        resultSet.next();

        assertEquals(username, resultSet.getString("username"));
    }

    @Test
    public void deleteExpireTimeForUsername() throws Exception {
        expireTime.createExpireTimeFor(username);
        expireTime.deleteExpireTimeFor(username);
        ResultSet resultSet = statement.executeQuery("select count(*) as counted from expire_time");
        resultSet.next();
        assertEquals(0, resultSet.getInt("counted"));
    }

    @Test
    public void getExpireTimeForUsername() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 3);
        Timestamp expectedTimestamp = new Timestamp(calendar.getTime().getTime());
        Timestamp actualTimestamp = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into expire_time(username,time) values (?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setTimestamp(2, expectedTimestamp);
            preparedStatement.execute();
            actualTimestamp = expireTime.getExpireTimeFor(username);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        assertEquals(expectedTimestamp.toString().substring(0, 20), actualTimestamp.toString().substring(0, 20));

    }
}
