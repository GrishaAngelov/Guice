package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class LoginServletTest {
    private LoginServlet loginServlet;
    private CredentialsValidator credentialsValidator;
    private UserRegistry userRegistry;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        credentialsValidator = context.mock(CredentialsValidator.class);
        userRegistry = context.mock(UserRegistry.class);
        loginServlet = new LoginServlet(credentialsValidator, userRegistry);
    }

    @Test
    public void happyPath() throws Exception {
        final HttpSession session = context.mock(HttpSession.class);
        final UserCookie cookie = new UserCookie("loginGreet", "Hello, John");

        context.checking(new Expectations() {{

            oneOf(request).getParameterMap();
            will(returnValue(new HashMap<String, String[]>() {{
                put("usernameBox", new String[]{"John"});
                put("passwordBox", new String[]{"123456"});
            }}));

            oneOf(credentialsValidator).validate("John", "123456");
            will(returnValue(true));

            oneOf(userRegistry).isUserRegistered("John", "123456");
            will(returnValue(true));

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("isLogged", true);

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("username", "John");
            oneOf(response).addCookie(cookie);

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getId();
            will(returnValue("9asd56"));

            oneOf(response).addCookie(cookie);

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setMaxInactiveInterval(180);

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("operationStatus", "");
            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/index.jsp");
        }});

        loginServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void tryLoginWithNonexistentCredentials() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).getParameterMap();
            will(returnValue(new HashMap<String, String[]>() {{
                put("usernameBox", new String[]{"Peter"});
                put("passwordBox", new String[]{"asd"});
            }}));


            oneOf(credentialsValidator).validate("Peter", "asd");
            will(returnValue(true));

            oneOf(userRegistry).isUserRegistered("Peter", "asd");
            will(returnValue(false));

            oneOf(request).setAttribute("labelMessage", "*Incorrect username or/and password");
            oneOf(request).getRequestDispatcher("/login.jsp");
        }});

        loginServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void tryLoginWithEmptyUsername() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).getParameterMap();
            will(returnValue(new HashMap<String, String[]>() {{
                put("usernameBox", new String[]{""});
                put("passwordBox", new String[]{"123456"});
            }}));

            oneOf(credentialsValidator).validate("", "123456");
            will(returnValue(false));

            oneOf(request).setAttribute("labelMessage", "*Please enter username and password");
            oneOf(request).getRequestDispatcher("/login.jsp");
        }});

        loginServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void tryLoginWithEmptyPassword() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).getParameterMap();
            will(returnValue(new HashMap<String, String[]>() {{
                put("usernameBox", new String[]{"John"});
                put("passwordBox", new String[]{""});
            }}));

            oneOf(credentialsValidator).validate("John", "");
            will(returnValue(false));

            oneOf(request).setAttribute("labelMessage", "*Please enter username and password");
            oneOf(request).getRequestDispatcher("/login.jsp");
        }});

        loginServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void tryLoginWithEmptyCredentials() throws Exception {

        context.checking(new Expectations() {{

            oneOf(request).getParameterMap();
            will(returnValue(new HashMap<String, String[]>() {{
                put("usernameBox", new String[]{""});
                put("passwordBox", new String[]{""});
            }}));

            oneOf(credentialsValidator).validate("", "");
            will(returnValue(false));

            oneOf(request).setAttribute("labelMessage", "*Please enter username and password");
            oneOf(request).getRequestDispatcher("/login.jsp");
        }});

        loginServlet.doPost(request, response);
        context.assertIsSatisfied();
    }
}
