package com.clouway.bank;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
@Singleton
public class BalanceServlet extends HttpServlet {
    private Account bankAccount;

    @Inject
    public BalanceServlet(Account bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = getUsernameFromCookie(req);
        BigDecimal amount;
        if (username != null) {
            amount = bankAccount.getBalance(username);
            req.setAttribute("balance", "Current Balance: " + amount);
            req.getRequestDispatcher("/balance.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/logout.jsp").forward(req, resp);
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
