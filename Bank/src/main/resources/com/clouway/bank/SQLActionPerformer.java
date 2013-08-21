package com.clouway.bank;

import java.sql.SQLException;

public class SQLActionPerformer {


    public <T> T executeSQLAction(Action<T> action) {
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
}