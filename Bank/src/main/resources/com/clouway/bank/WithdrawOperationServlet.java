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
public class WithdrawOperationServlet extends HttpServlet {
    private Account bankAccount;
    @Inject @Named("limit")double limitAmountValue;

    @Inject
    public WithdrawOperationServlet(Account bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String withdrawAmount = checkDelimiter(request.getParameter("withdrawAmount"));
        String username = getUsernameFromCookie(request);
        boolean isMatch = withdrawAmount.matches("^[0-9]+$|^[0-9]+[.][0-9]{2}$");

        try {
            if (isMatch) {
                boolean isSuccessful = bankAccount.withdraw(withdrawAmount, username);
                if (!isSuccessful) {
                    request.setAttribute("operationStatus", "Can not perform operation! Not enough money in your account!");
                } else {
                    request.setAttribute("operationStatus", "Amount successfully withdrawn!");
                }
                request.getRequestDispatcher("/withdraw.jsp").forward(request,response);
            } else {
                request.setAttribute("operationStatus", "Amount not withdrawn!");
                request.getRequestDispatcher("/withdraw.jsp").forward(request,response);
            }
        } catch (IncorrectAmountValueException e) {
            request.setAttribute("operationStatus", "Amount not withdrawn!");
            request.getRequestDispatcher("/withdraw.jsp").forward(request,response);
        }
    }

    private String checkDelimiter(String amount) {
        if (amount.contains(",")) {
            amount = amount.replace(',', '.');
        }
        if (amount.contains(".") && amount.indexOf(".") + 3 != amount.length()) {
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
