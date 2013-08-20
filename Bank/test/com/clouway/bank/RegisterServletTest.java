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
public class RegisterServletTest {
    private RegisterServlet registerServlet;
    private UserRepository userRepository;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        userRepository = context.mock(UserRepository.class);
        registerServlet = new RegisterServlet(userRepository);
    }

    @Test
    public void registerUserHappyPath() throws Exception {
        final HttpSession session = context.mock(HttpSession.class);

        context.checking(new Expectations(){{
            oneOf(request).getParameter("usernameBox");
            will(returnValue("John"));

            oneOf(request).getParameter("passwordBox");
            will(returnValue("123456"));

            oneOf(userRepository).add(new User("John", "123456"));
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("username","John");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/successfulRegistration.jsp");
        }});

        registerServlet.doPost(request,response);
        context.assertIsSatisfied();
    }

    @Test
    public void missingUsername() throws Exception {

        context.checking(new Expectations(){{
            oneOf(request).getParameter("usernameBox");
            will(returnValue(""));

            oneOf(request).getParameter("passwordBox");
            will(returnValue("123456"));

            oneOf(userRepository).add(new User("", "123456"));
            will(returnValue(false));

            oneOf(request).getContextPath();
            one(response).sendRedirect("/failedRegistration.jsp");

        }});

        registerServlet.doPost(request,response);
        context.assertIsSatisfied();
    }

    @Test
    public void missingPassword() throws Exception {

        context.checking(new Expectations(){{
            oneOf(request).getParameter("usernameBox");
            will(returnValue("John"));

            oneOf(request).getParameter("passwordBox");
            will(returnValue(""));

            oneOf(userRepository).add(new User("John", ""));
            will(returnValue(false));

            oneOf(request).getContextPath();
            one(response).sendRedirect("/failedRegistration.jsp");

        }});

        registerServlet.doPost(request,response);
        context.assertIsSatisfied();
    }
}
