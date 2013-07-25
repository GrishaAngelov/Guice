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

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class DepositOperationServletTest {
    private DepositOperationServlet depositOperationServlet;
    private Account bankAccount;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private final double limitAmountValue = 99999999.0;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        bankAccount = context.mock(Account.class);
        depositOperationServlet = new DepositOperationServlet(bankAccount, limitAmountValue);
    }

    @Test
    public void depositHappyPath() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("20", "John");
            will(returnValue(true));


            oneOf(request).setAttribute("operationStatus", "Amount successfully added!");
            one(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithDotDelimiter() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");

        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithCommaDelimiter() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20,00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithOnePlaceAfterDotDecimalPoint() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.0"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithOnePlaceAfterCommaDecimalPoint() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20,0"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositTooBigAmount() throws IOException, ServletException {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("9999999999999999999999"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("9999999999999999999999", "John");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositNegativeAmount() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("-20"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("-20", "John");
            will(throwException(new IncorrectAmountValueException()));


            oneOf(request).setAttribute("operationStatus", "Amount not added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test(expected = NoSuchUserException.class)
    public void depositToAccountWithEmptyName() throws IOException, ServletException {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "")};
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("20.00", "");
            will(throwException(new NoSuchUserException()));
        }});
        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositStringAmount() throws IOException, ServletException {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("asd"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(bankAccount).deposit("asd", "John");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }
}
