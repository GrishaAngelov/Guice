package com.clouway.bank;

import com.google.inject.Provider;
import org.apache.commons.dbcp.PoolableConnection;


/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */

public class ConnectionProvider implements Provider<PoolableConnection> {

    public void setUpDatabaseConnection(String url, String user, String password) {
        ConnectionFilter.setUpDatabaseConnection(url, user, password);
    }

    @Override
    public PoolableConnection get() {
        return ConnectionFilter.get();
    }
}
