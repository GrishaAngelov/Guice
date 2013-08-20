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
public class WithdrawOperationServletTest {
    private WithdrawOperationServlet withdrawOperationServlet;
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
        withdrawOperationServlet = new WithdrawOperationServlet(bankAccount,amountValidator,new Messages());
    }

    @Test
    public void withdrawAmountHappyPath() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        final BigDecimal amount = new BigDecimal("100");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("100"));

            oneOf(amountValidator).checkAmountDelimiter("100");
            will(returnValue("100"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);
            oneOf(bankAccount).withdraw(amount, "John");
            will(returnValue(true));

            oneOf(request).setAttribute("operationStatus", "Amount successfully withdrawn!");

            oneOf(request).getRequestDispatcher("/withdraw.jsp");
        }});

        withdrawOperationServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawZeroAmount() throws IOException, ServletException {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("0"));

            oneOf(amountValidator).checkAmountDelimiter("0");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not withdrawn! Enter only positive amount.");
            oneOf(request).getRequestDispatcher("/withdraw.jsp");
        }});

        withdrawOperationServlet.doPost(request, response);
        context.assertIsSatisfied();

    }

    @Test
    public void withdrawNegativeAmount() throws Exception {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("-100"));

            oneOf(amountValidator).checkAmountDelimiter("-100");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not withdrawn! Enter only positive amount.");
            oneOf(request).getRequestDispatcher("/withdraw.jsp");

        }});

        withdrawOperationServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithDotDelimiter() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20.00"));

            oneOf(amountValidator).checkAmountDelimiter("20.00");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).withdraw(amount, "John");
            will(returnValue(true));


            oneOf(request).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getRequestDispatcher("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithCommaDelimiter() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20,00"));

            oneOf(amountValidator).checkAmountDelimiter("20,00");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).withdraw(amount, "John");
            will(returnValue(true));


            oneOf(request).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getRequestDispatcher("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithOnePlaceAfterDotDecimalPoint() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20.0"));

            oneOf(amountValidator).checkAmountDelimiter("20.0");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).withdraw(amount, "John");
            will(returnValue(true));


            oneOf(request).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getRequestDispatcher("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithOnePlaceAfterCommaDecimalPoint() throws Exception {
        final Cookie[] cookies = {new Cookie("expireTimeCookie", "John&2013-07-25 13:21:55")};
        final BigDecimal amount = new BigDecimal("20.00");
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20,0"));

            oneOf(amountValidator).checkAmountDelimiter("20,0");
            will(returnValue("20.00"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(amountValidator).validate(amount);

            oneOf(bankAccount).withdraw(amount, "John");
            will(returnValue(true));

            oneOf(request).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getRequestDispatcher("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawTooBigAmount() throws IOException, ServletException {
        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("9999999999999999999999"));

            oneOf(amountValidator).checkAmountDelimiter("9999999999999999999999");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).setAttribute("operationStatus", "Amount not withdrawn! Enter only positive amount.");
            oneOf(request).getRequestDispatcher("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }
}
