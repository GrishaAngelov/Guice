package com.clouway.bank;

import org.apache.commons.dbcp.PoolableConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */

public class BankUserRepositoryTest {
    private BankUserRepository bankUserRegistry;
    private PoolableConnection connection;
    private Statement statement;


    @Rule
    public ConnectionRule connectionRule = new ConnectionRule();

    @Before
    public void setUp() throws Exception {
        bankUserRegistry = new BankUserRepository(new SQLActionPerformer());
        connection = connectionRule.get();
        statement = connection.createStatement();
        statement.execute("create table if not exists users(username varchar(20) not null primary key, password varchar (10))");
        statement.execute("insert into users values (\"Bob\",\"123456\")");
    }

    @After
    public void tearDown() throws Exception {
        statement.execute("drop table users");
        statement.close();
        connection.close();
    }

    @Test
    public void checkIsUserRegisteredHappyPath() throws SQLException {
        assertTrue(bankUserRegistry.hasUser(new User("Bob", "123456")));
    }

    @Test
    public void checkRegisteredUserWithWrongPassword() throws Exception {
        assertFalse(bankUserRegistry.hasUser(new User("Bob", "asd")));
    }


    @Test
    public void checkRegisteredUserWithDifferentCapitalizedUsernameAndSamePassword() throws Exception {
        List<String> usernameList = Arrays.asList("bob","bOb","boB","BOb","bOB","BoB","BOB");

        for (int i = 0; i < usernameList.size(); i++) {
           String username = usernameList.get(i);
           assertFalse(String.format("Error in name #%d - %s",i,username), bankUserRegistry.hasUser(new User(username, "123456")));
        }
    }

    @Test
    public void checkForRegistrationWithNotRegisteredUser() throws Exception {
        assertFalse(bankUserRegistry.hasUser(new User("Peter", "123")));
    }
}
