package com.clouway.bank;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
@Singleton
public class LogoutServlet extends HttpServlet {

    private ExpireTime expireTime;

    @Inject
    public LogoutServlet(ExpireTime expireTime) {
        this.expireTime = expireTime;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().invalidate();
        deleteCookies(request, response);
        expireTime.deleteExpireTimeFor(getUsernameFromCookie(request));
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    private void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JSESSIONID")) {
                Cookie sessionCookie = new Cookie("JSESSIONID", null);
                sessionCookie.setPath(request.getContextPath());
                sessionCookie.setMaxAge(0);
                response.addCookie(sessionCookie);
            }
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    private String getUsernameFromCookie(HttpServletRequest request) {
        String username = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("expireTimeCookie")) {
                    username = c.getValue().split("&")[0];
                }
            }
        }
        return username;
    }
}
