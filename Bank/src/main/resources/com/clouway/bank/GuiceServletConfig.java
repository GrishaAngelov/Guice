package com.clouway.bank;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import org.apache.commons.dbcp.PoolableConnection;

/**
 * @author grisha_angelov
 */
public class GuiceServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                bind(Validator.class).to(UserValidator.class);
                bind(UserRepository.class).to(BankUserRepository.class);
                bind(Account.class).to(BankAccount.class);
                bind(ExpireTime.class).to(SessionExpireTime.class);
                bind(Double.class).annotatedWith(Names.named("limit")).toInstance(99999999.0);
                bind(PoolableConnection.class).toProvider(ConnectionProvider.class);
                bind(AmountValidator.class).to(AmountValueValidator.class);

                bind(String.class).annotatedWith(Names.named("user")).toInstance("root");
                bind(String.class).annotatedWith(Names.named("databaseUrl")).toInstance("jdbc:mysql://localhost:3306/bank");
                bind(String.class).annotatedWith(Names.named("password")).toInstance("123456");

                serve("/LoginServlet").with(LoginServlet.class);
                serve("/IndexServlet").with(IndexServlet.class);
                serve("/RegisterServlet").with(RegisterServlet.class);
                serve("/BalanceServlet").with(BalanceServlet.class);
                serve("/DepositServlet").with(DepositServlet.class);
                serve("/DepositOperationServlet").with(DepositOperationServlet.class);
                serve("/WithdrawServlet").with(WithdrawServlet.class);
                serve("/WithdrawOperationServlet").with(WithdrawOperationServlet.class);
                serve("/LogoutServlet").with(LogoutServlet.class);

                filter("/RegisterServlet").through(RegisterFilter.class);

                filter("/BalanceServlet").through(LoginFilter.class);
                filter("/DepositServlet").through(LoginFilter.class);
                filter("/WithdrawServlet").through(LoginFilter.class);

                filter("/index.jsp").through(LoginFilter.class);

                filter("/BalanceServlet").through(ConnectionFilter.class);
                filter("/DepositServlet").through(ConnectionFilter.class);
                filter("/WithdrawServlet").through(ConnectionFilter.class);
            }
        });
    }
}
