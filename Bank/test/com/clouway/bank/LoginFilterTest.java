package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;

/**
* @author Grisha Angelov <grisha.angelov@clouway.com>
*/
public class LoginFilterTest {
    private LoginFilter loginFilter;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private ExpireTime expireTime;
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        filterChain = context.mock(FilterChain.class);
        expireTime = context.mock(ExpireTime.class);
        session = context.mock(HttpSession.class);
        loginFilter = new LoginFilter(expireTime);
    }

    @Test
    public void checkUserIsLoggedInWithNotExpiredTime() throws Exception {

        context.checking(new Expectations() {{
            Cookie[] cookies = {new Cookie("expireTimeCookie", "pesho&2014-07-25 13:21:55")};

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("pesho"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(filterChain).doFilter(request, response);
        }});

        loginFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }

    @Test
    public void checkUserIsLoggedInWithExpiredTime() throws Exception {

        context.checking(new Expectations() {{
            Cookie[] cookies = {new Cookie("expireTimeCookie", "pesho&2013-07-25 13:21:55")};

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("username");
            will(returnValue("pesho"));

            oneOf(request).getCookies();
            will(returnValue(cookies));

            oneOf(expireTime).deleteExpireTimeFor("pesho");

            oneOf(request).getRequestDispatcher("/logout.jsp");
        }});

        loginFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }

    @Test
    public void checkUserIsNotLoggedIn() throws Exception {

        context.checking(new Expectations() {{
            oneOf(request).getCookies();
            will(returnValue(null));
            oneOf(request).getRequestDispatcher("/login.jsp");
        }});

        loginFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }


}
