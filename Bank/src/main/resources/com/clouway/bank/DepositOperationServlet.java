package com.clouway.bank;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.inject.Named;
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
public class DepositOperationServlet extends HttpServlet {
    private Account bankAccount;
    private double limitAmountValue;

    @Inject
    public DepositOperationServlet(Account bankAccount, @Named("limit")double limitAmountValue) {
        this.bankAccount = bankAccount;
        this.limitAmountValue = limitAmountValue;
    }

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String deposit = checkDelimiter(request.getParameter("deposit"));
        String username = getUsernameFromCookie(request);
        try {

            boolean isSuccessful = bankAccount.deposit(deposit, username);

            if (isSuccessful) {
                request.setAttribute("operationStatus", "Amount successfully added!");
            } else {
                request.setAttribute("operationStatus", "Amount not added!");
            }
        } catch (IncorrectAmountValueException e) {
            request.setAttribute("operationStatus", "Amount not added!");
        }
        request.getRequestDispatcher("/deposit.jsp").forward(request,response);
    }

    public void destroy() {

    }

    private String checkDelimiter(String amount) {
        final int offset = 3;
        if (amount.contains(",")) {
            amount = amount.replace(',', '.');
        }
        if (amount.contains(".") && amount.indexOf(".") + offset != amount.length()) {
            amount = amount.concat("0");
        }
        return amount;
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
