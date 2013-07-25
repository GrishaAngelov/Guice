package com.clouway.bank;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
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
    //    private final int expireTime = 3 * 60; // in seconds
    private CredentialsValidator userCredentialsValidator;
    private UserRegistry bankUserRegistry;
    private ExpireTime sessionExpireTime;

    @Inject
    public LoginServlet(CredentialsValidator userCredentialsValidator, UserRegistry bankUserRegistry, ExpireTime sessionExpireTime) {
        this.userCredentialsValidator = userCredentialsValidator;
        this.bankUserRegistry = bankUserRegistry;
        this.sessionExpireTime = sessionExpireTime;
    }

    public void init(ServletConfig config) {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Map<String, String[]> map = request.getParameterMap();

        String username = map.get("usernameBox")[0];
        String password = map.get("passwordBox")[0];

//        String username = request.getParameter("usernameBox");
//        String password  = request.getParameter("passwordBox");

        if (!userCredentialsValidator.isValid(username, password)) {
            request.setAttribute("labelMessage", "*Please enter username and password");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } else {
            if (bankUserRegistry.isUserRegistered(username, password)) {

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

    public void destroy() {

    }

    private void createCookies(HttpServletRequest request, HttpServletResponse response, String username) {


        UserCookie loginCookie = new UserCookie("loginGreet", "Hello, " + username);
//        loginCookie.setMaxAge(expireTime);
        response.addCookie(loginCookie);

        UserCookie jSessionIdCookie = new UserCookie("JSESSIONID", request.getSession().getId());
//        jSessionIdCookie.setMaxAge(expireTime);
        response.addCookie(jSessionIdCookie);

        String expireTimeCookieContent = String.format("%s&%s", username, sessionExpireTime.getExpireTimeFor(username).toString());
        UserCookie expireTimeCookie = new UserCookie("expireTimeCookie", expireTimeCookieContent);
        response.addCookie(expireTimeCookie);
    }
}
