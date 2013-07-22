package com.clouway.bank;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.servlet.ServletException;
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

    @Inject
    public DepositOperationServlet(Account bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String deposit = checkDelimiter(request.getParameter("deposit"));
        String username = String.valueOf(request.getSession().getAttribute("username"));
        try {
            boolean isSuccessful = bankAccount.deposit(deposit, username);

            if (isSuccessful) {
                request.getSession().setAttribute("operationStatus", "Amount successfully added!");
            } else {
                request.getSession().setAttribute("operationStatus", "Amount not added!");
            }
        } catch (IncorrectAmountValueException e) {
            request.getSession().setAttribute("operationStatus", "Amount not added!");
        }
        response.sendRedirect(request.getContextPath() + "/deposit.jsp");
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
}
