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
public class DepositOperationServletTest {
    private DepositOperationServlet depositOperationServlet;
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
        depositOperationServlet = new DepositOperationServlet(bankAccount);
    }

    @Test
    public void depositHappyPath() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("20", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithDotDelimiter() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.00"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithCommaDelimiter() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20,00"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithOnePlaceAfterDotDecimalPoint() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.0"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositAmountWithOnePlaceAfterCommaDecimalPoint() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20,0"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("20.00", "John");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount successfully added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositTooBigAmount() throws IOException, ServletException {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("9999999999999999999999"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("9999999999999999999999", "John");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount not added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositNegativeAmount() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("-20"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("-20", "John");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount not added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test (expected = NoSuchUserException.class)
    public void depositToAccountWithEmptyName() throws IOException, ServletException {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("20.00"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue(""));

            oneOf(bankAccount).deposit("20.00", "");
            will(throwException(new NoSuchUserException()));
        }});
        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void depositStringAmount() throws IOException, ServletException {

        context.checking(new Expectations() {{
            oneOf(request).getParameter("deposit");
            will(returnValue("asd"));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("John"));

            oneOf(bankAccount).deposit("asd","John");
            will(throwException(new IncorrectAmountValueException()));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "Amount not added!");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/deposit.jsp");
        }});

        depositOperationServlet.doGet(request, response);
        context.assertIsSatisfied();
    }
}
