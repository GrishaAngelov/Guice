package com.clouway.bank;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */

@Singleton
public class LoginServlet extends HttpServlet {
    private CredentialsValidator userCredentialsValidator;
    private UserRepository bankUserRepository;
    private ExpireTime sessionExpireTime;

    @Inject
    public LoginServlet(CredentialsValidator userCredentialsValidator, UserRepository bankUserRepository, ExpireTime sessionExpireTime) {
        this.userCredentialsValidator = userCredentialsValidator;
        this.bankUserRepository = bankUserRepository;
        this.sessionExpireTime = sessionExpireTime;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, String[]> map = request.getParameterMap();

        String username = map.get("usernameBox")[0];
        String password = map.get("passwordBox")[0];

        User user = new User(username, password);

        if (!userCredentialsValidator.isValid(user)) {
            request.setAttribute("labelMessage", "*Please enter username and password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            if (bankUserRepository.hasUser(user)) {

                sessionExpireTime.createExpireTimeFor(username);

                request.getSession().setAttribute("username", username);

                createCookies(request, response, username);

                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                request.setAttribute("labelMessage", "*Incorrect username or/and password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }
    }

    private void createCookies(HttpServletRequest request, HttpServletResponse response, String username) {


        UserCookie loginCookie = new UserCookie("loginGreet", "Hello, " + username);
        response.addCookie(loginCookie);

        UserCookie jSessionIdCookie = new UserCookie("JSESSIONID", request.getSession().getId());
        response.addCookie(jSessionIdCookie);

        String expireTimeCookieContent = String.format("%s&%s", username, sessionExpireTime.getExpireTimeFor(username).toString());
        UserCookie expireTimeCookie = new UserCookie("expireTimeCookie", expireTimeCookieContent);
        response.addCookie(expireTimeCookie);
    }
}
