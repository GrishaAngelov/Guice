package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class DepositOperationServletTest {
    private DepositOperationServlet depositOperationServlet;
    private Account bankAccount;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AmountValidator amountValidator;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        bankAccount = context.mock(Account.class);
        amountValidator = context.mock(AmountValidator.class);
        depositOperationServlet = new DepositOperationServlet(amountValidator, bankAccount, new Messages());
    }

    @Test
    public void depositHappyPath() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20"));

            oneOf(amountValidator).checkAmountDelimiter("20");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).deposit(amount, "John");
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
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.00"));

            oneOf(amountValidator).checkAmountDelimiter("20.00");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).deposit(amount, "John");
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
        final BigDecimal amount = new BigDecimal("20.00");

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20,00"));

            oneOf(amountValidator).checkAmountDelimiter("20,00");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).deposit(amount, "John");
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
        final BigDecimal amount = new BigDecimal("20.00");

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.0"));

            oneOf(amountValidator).checkAmountDelimiter("20.0");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);
            oneOf(bankAccount).deposit(amount, "John");
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
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20,0"));

            oneOf(amountValidator).checkAmountDelimiter("20,0");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).deposit(amount, "John");
            will(returnValue(true));

            oneOf(request).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositTooBigAmount() throws IOException, ServletException {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("9999999999999999999999"));

            oneOf(amountValidator).checkAmountDelimiter("9999999999999999999999");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not added! Enter only positive amount.");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositNegativeAmount() throws Exception {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("-20"));

            oneOf(amountValidator).checkAmountDelimiter("-20");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not added! Enter only positive amount.");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositZeroAmount() throws IOException, ServletException {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("0"));

            oneOf(amountValidator).checkAmountDelimiter("0");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not added! Enter only positive amount.");
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }


    @Test(expected = UserNotFoundException.class)
    public void depositToAccountWithEmptyName() throws IOException, ServletException {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "")};
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.00"));

            oneOf(amountValidator).checkAmountDelimiter("20.00");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).deposit(amount, "");
            will(throwException(new UserNotFoundException()));
        }});
        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }


}
