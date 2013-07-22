package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class WithdrawOperationServletTest {
    private WithdrawOperationServlet withdrawOperationServlet;
    private Account bankAccount;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        session = context.mock(HttpSession.class);
        bankAccount = context.mock(Account.class);
        withdrawOperationServlet = new WithdrawOperationServlet(bankAccount);
    }

    @Test
    public void withdrawAmountHappyPath() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("100"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).withdraw("100", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getContextPath();

            oneOf(response).sendRedirect("/withdraw.jsp");
        }});

        withdrawOperationServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawZeroAmount() throws IOException, ServletException {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("0"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).withdraw("0","John");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount not withdrawn!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/withdraw.jsp");
        }});

        withdrawOperationServlet.doPost(request, response);
        context.assertIsSatisfied();

    }

    @Test
    public void withdrawNegativeAmount() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("-100"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount not withdrawn!");

            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/withdraw.jsp");

        }});

        withdrawOperationServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithDotDelimiter() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20.00"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).withdraw("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithCommaDelimiter() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20,00"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).withdraw("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithOnePlaceAfterDotDecimalPoint() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20.0"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).withdraw("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawAmountWithOnePlaceAfterCommaDecimalPoint() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("20,0"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).withdraw("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully withdrawn!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void withdrawTooBigAmount() throws IOException, ServletException {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("withdrawAmount");
            will(returnValue("9999999999999999999999"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).withdraw("9999999999999999999999", "John");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount not withdrawn!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/withdraw.jsp");
        }});

        withdrawOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }
}
