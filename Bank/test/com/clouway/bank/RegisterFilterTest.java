package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class RegisterFilterTest {
    private RegisterFilter registerFilter;
    private Validator validator;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        filterChain = context.mock(FilterChain.class);
        validator = context.mock(Validator.class);
        registerFilter = new RegisterFilter(validator);
    }

    @Test
    public void validateCredentialsHappyPath() throws Exception {

        context.checking(new Expectations(){{
            oneOf(request).getParameter("usernameBox");
            will(returnValue("John"));

            oneOf(request).getParameter("passwordBox");
            will(returnValue("123456"));

            oneOf(validator).isValid(new User("John","123456"));
            will(returnValue(true));

            oneOf(filterChain).doFilter(request,response);
        }});

        registerFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }

    @Test
    public void missingUsername() throws Exception {

        context.checking(new Expectations(){{
            oneOf(request).getParameter("usernameBox");
            will(returnValue(""));

            oneOf(request).getParameter("passwordBox");
            will(returnValue("123456"));

            oneOf(validator).isValid(new User("","123456"));
            will(returnValue(false));

            oneOf(request).setAttribute("message", "Please enter username and/or password");
            oneOf(request).getRequestDispatcher("/registration.jsp");
        }});

        registerFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }

    @Test
    public void missingPassword() throws Exception {

        context.checking(new Expectations(){{
            oneOf(request).getParameter("usernameBox");
            will(returnValue("John"));

            oneOf(request).getParameter("passwordBox");
            will(returnValue(""));

            oneOf(validator).isValid(new User("John",""));
            will(returnValue(false));

            oneOf(request).setAttribute("message", "Please enter username and/or password");
            oneOf(request).getRequestDispatcher("/registration.jsp");
        }});

        registerFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }

    @Test
    public void emptyCredentials() throws IOException, ServletException {

        context.checking(new Expectations(){{
            oneOf(request).getParameter("usernameBox");
            will(returnValue(""));

            oneOf(request).getParameter("passwordBox");
            will(returnValue(""));

            oneOf(validator).isValid(new User("",""));
            will(returnValue(false));

            oneOf(request).setAttribute("message", "Please enter username and/or password");
            oneOf(request).getRequestDispatcher("/registration.jsp");
        }});

        registerFilter.doFilter(request, response, filterChain);
        context.assertIsSatisfied();
    }
}
