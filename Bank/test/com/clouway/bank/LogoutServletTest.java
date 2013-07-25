package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* @author Grisha Angelov <grisha.angelov@clouway.com>
*/
public class LogoutServletTest {
    private LogoutServlet logoutServlet;
    private ExpireTime expireTime;
    private Mockery context;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        expireTime = context.mock(ExpireTime.class);
        logoutServlet = new LogoutServlet(expireTime);
    }

    @Test
    public void logoutUser() throws Exception {
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        final HttpSession session = context.mock(HttpSession.class);

        context.checking(new Expectations(){{

            Cookie[] cookies = {new Cookie("expireTimeCookie", "pesho&2013-07-25 13:21:55")};

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).invalidate();

            oneOf(response).addCookie(cookies[0]);

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(expireTime).deleteExpireTimeFor("pesho");

            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/index.jsp");
        }});

        logoutServlet.doPost(request, response);
        context.assertIsSatisfied();
    }
}
