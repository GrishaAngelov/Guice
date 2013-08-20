package com.clouway.bank;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.CookieStore;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
@Singleton
public class LoginFilter implements Filter {

    private ExpireTime sessionExpireTime;

    @Inject
    public LoginFilter(ExpireTime expireTime) {
        this.sessionExpireTime = expireTime;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getCookies() == null) {
            if (request.getAttribute("greet") == null) {
                request.setAttribute("greet", "");
            }
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } else {
            String username = String.valueOf(request.getSession().getAttribute("username"));
            if (!isUserSessionExpired(request)) {

                Cookie[] cookies = request.getCookies();
                String cookieValue = "";

                Cookie greetCookie = null;
                if (cookies != null) {
                    for (int i = 0; i < cookies.length; i++) {
                        if (cookies[i].getName().equals("loginGreet")) {
                            greetCookie = cookies[i];
                            break;
                        }
                    }
                    cookieValue = greetCookie.getValue();
                    cookieValue += "<br/><a style='color:white;' href='logout.jsp'>logout</a>";
                }
                request.setAttribute("greetCookie", greetCookie);
                request.setAttribute("greet", cookieValue);

                filterChain.doFilter(request, response);
            } else {
                sessionExpireTime.deleteExpireTimeFor(username);
                request.getRequestDispatcher("/logout.jsp").forward(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isUserSessionExpired(HttpServletRequest request) {
        Timestamp currentTime = new Timestamp(Calendar.getInstance().getTime().getTime());
//        Timestamp expireTime = sessionExpireTime.getExpireTimeFor(username);
        Timestamp expireTime = getExpireTimeFromCookie(request);

        return currentTime.after(expireTime);
    }

    private Timestamp getExpireTimeFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = "";
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("expireTimeCookie")) {
                    cookieValue = c.getValue().split("&")[1];
                }
            }
        }
        return Timestamp.valueOf(cookieValue);
    }
}
