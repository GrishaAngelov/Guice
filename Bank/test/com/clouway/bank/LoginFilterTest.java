package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class LoginFilterTest {
    private LoginFilter loginFilter;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        loginFilter = new LoginFilter();
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        filterChain = context.mock(FilterChain.class);
        session = context.mock(HttpSession.class);
    }

    @Test
    public void checkUserIsLoggedIn() throws Exception {


        context.checking(new Expectations(){{
            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("isLogged");
            will(returnValue(true));

            oneOf(filterChain).doFilter(request,response);
            oneOf(request).getAttribute("operationStatus");
        }});

        loginFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }

    @Test
    public void checkUserIsNotLoggedIn() throws Exception {

        context.checking(new Expectations(){{
            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getAttribute("isLogged");
            will(returnValue(null));

            oneOf(request).getRequestDispatcher("/login.jsp");
            oneOf(request).getAttribute("operationStatus");
        }});

        loginFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }


}
