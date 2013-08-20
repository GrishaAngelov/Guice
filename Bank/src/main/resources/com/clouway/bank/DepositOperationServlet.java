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
public class DepositOperationServlet extends HttpServlet {
    private Account bankAccount;
    private AmountValidator amountValidator;
    private Messages messages;

    @Inject
    public DepositOperationServlet(AmountValidator amountValidator, Account bankAccount, Messages messages) {
        this.bankAccount = bankAccount;
        this.amountValidator = amountValidator;
        this.messages = messages;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            String deposit = amountValidator.checkAmountDelimiter(request.getParameter("deposit"));
            String username = getUsernameFromCookie(request);

            BigDecimal depositAmount = new BigDecimal(deposit);

            amountValidator.validate(depositAmount);

            boolean isDepositSuccessful = bankAccount.deposit(depositAmount, username);

            if (isDepositSuccessful) {
                request.setAttribute("operationStatus", messages.successfullyAddedAmount());
            } else {
                request.setAttribute("operationStatus", messages.addAmountWithTooManyDecimalPlacesError());
            }
        } catch (IncorrectAmountValueException e) {
            request.setAttribute("operationStatus", messages.addNegativeAmountError());
        } catch (NumberFormatException e) {
            request.setAttribute("operationStatus", messages.addStringAmountError());
        }
        request.getRequestDispatcher("/deposit.jsp").forward(request, response);
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
