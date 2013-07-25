package com.clouway.bank;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class ConnectionProviderRule implements TestRule {
    private ConnectionProvider connectionProvider;

    public ConnectionProviderRule() {
        connectionProvider = new ConnectionProvider();
        connectionProvider.setUpDatabaseConnection("jdbc:mysql://localhost:3306/testbank", "root", "123456");
    }

    public ConnectionProvider get(){
        return connectionProvider;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new ConnectionStatement(statement);
    }

    class ConnectionStatement extends Statement {
        private Statement statement;

        public ConnectionStatement(Statement statement) {
            this.statement = statement;
        }

        @Override
        public void evaluate() throws Throwable {
            statement.evaluate();
        }
    }
}
