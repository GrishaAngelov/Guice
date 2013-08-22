package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;

/**
* @author Grisha Angelov <grisha.angelov@clouway.com>
*/
public class LoginServletTest {
    private LoginServlet loginServlet;
    private Validator validator;
    private UserRepository userRepository;
    private ExpireTime sessionExpireTime;
    private Mockery context;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        context = new Mockery();
        request = context.mock(HttpServletRequest.class);
        response = context.mock(HttpServletResponse.class);
        validator = context.mock(Validator.class);
        sessionExpireTime = context.mock(ExpireTime.class);
        userRepository = context.mock(UserRepository.class);
        loginServlet = new LoginServlet(validator, userRepository, sessionExpireTime);
    }

    @Test
    public void happyPath() throws Exception {
        final HttpSession session = context.mock(HttpSession.class);
        final Cookie cookie = new UserCookie("asd","123");
        final User user = new User("John", "123456");

        context.checking(new Expectations() {{

            oneOf(request).getParameterMap();
            will(returnValue(new HashMap<String, String[]>() {{
                put("usernameBox", new String[]{"John"});
                put("passwordBox", new String[]{"123456"});
            }}));

            oneOf(validator).isValid(user);
            will(returnValue(true));

            oneOf(userRepository).hasUser(new User("John", "123456"));
            will(returnValue(true));

            oneOf(sessionExpireTime).createExpireTimeFor("John");

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).setAttribute("username","John");

            oneOf(response).addCookie(cookie);

            oneOf(request).getSession();
            will(returnValue(session));

            oneOf(session).getId();
            will(returnValue("asd"));

            oneOf(response).addCookie(cookie);

            oneOf(sessionExpireTime).getExpireTimeFor("John");
            will(returnValue(Timestamp.valueOf("2013-07-25 12:06:55")));

            oneOf(response).addCookie(cookie);

            oneOf(request).getContextPath();
            oneOf(response).sendRedirect("/index.jsp");

        }});

        loginServlet.doPost(request, response);
        context.assertIsSatisfied();
    }

    @Test
    public void tryLoginWithNonexistentCredentials() throws Exception {
        final User user = new User("Peter", "asd");
        context.checking(new Expectations() {{

            oneOf(request).getParameterMap();
            will(returnValue(new HashMap<String, String[]>() {{
                put("usernameBox", new String[]{"Peter"});
                put("passwordBox", new String[]{"asd"});
            }}));


            oneOf(validator).isValid(user);
            will(returnValue(true));

            oneOf(userRepository).hasUser(user);
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

            oneOf(validator).isValid(new User("", "123456"));
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

            oneOf(validator).isValid(new User("John", ""));
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

            oneOf(validator).isValid(new User("", ""));
            will(returnValue(false));

            oneOf(request).setAttribute("labelMessage", "*Please enter username and password");
            oneOf(request).getRequestDispatcher("/login.jsp");
        }});

        loginServlet.doPost(request, response);
        context.assertIsSatisfied();
    }
}
