package com.clouway.bank;

import org.apache.commons.dbcp.PoolableConnection;

import java.sql.SQLException;

/**
* @author Grisha Angelov <grisha.angelov@clouway.com>
*/
abstract class SQLAction<T> implements Action {

    private PoolableConnection connection;

    SQLAction(PoolableConnection connection) {
        this.connection = connection;
    }

    @Override
    public void finish() throws SQLException {
        connection.close();
    }
}
