package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class LogoutServletTest {
    private LogoutServlet logoutServlet;
    private Mockery context;

    @Before
    public void setUp() throws Exception {
        logoutServlet = new LogoutServlet();
        context = new Mockery();
    }

    @Test
    public void logoutUser() throws Exception {
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final HttpSession session = context.mock(HttpSession.class);

        context.checking(new Expectations(){{
            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).removeAttribute("isLogged");

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).invalidate();
            oneOf(request).getCookies();
            oneOf(request).getContextPath();
//            oneOf(response).addCookie(new UserCookie("asd","asd"));
//            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/index.jsp");
        }});

        logoutServlet.doPost(request, response);
        context.assertIsSatisfied();
    }
}
