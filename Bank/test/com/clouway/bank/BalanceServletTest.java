package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class BalanceServletTest {
    private BalanceServlet balanceServlet;
    private Account bankAccount;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @Before
    public void setUp() {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        session = context.mock(HttpSession.class);
        bankAccount = context.mock(Account.class);
        balanceServlet = new BalanceServlet(bankAccount);
    }

    @Test
    public void getBalanceForExistingUser() throws IOException, ServletException {

        context.checking(new Expectations() {{
            Cookie[] cookies = {new Cookie("expireTimeCookie", "pesho&2013-07-25 13:21:55")};
            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).getBalance("pesho");
            will(returnValue(new BigDecimal("20.00")));

            oneOf(request).setAttribute("balance", "Current Balance: 20.00");
            oneOf(request).getRequestDispatcher("/balance.jsp");

        }});
        balanceServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test(expected = UserNotFoundException.class)
    public void getBalanceForUserWithEmptyUsername() throws IOException, ServletException {

        context.checking(new Expectations() {{
            Cookie[] cookies = {new Cookie("expireTimeCookie", "")};

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).getBalance("");
            will(throwException(new UserNotFoundException()));
        }});
        balanceServlet.doGet(request, response);
        context.assertIsSatisfied();
    }
}
