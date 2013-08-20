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
import java.math.BigDecimal;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
@Singleton
public class WithdrawOperationServlet extends HttpServlet {
    private Account bankAccount;
    @Inject
    @Named("limit")
    double limitAmountValue;
    private AmountValidator amountValidator;
    private Messages messages;

    @Inject
    public WithdrawOperationServlet(Account bankAccount, AmountValidator amountValidator, Messages messages) {
        this.bankAccount = bankAccount;
        this.amountValidator = amountValidator;
        this.messages = messages;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String withdraw = amountValidator.checkAmountDelimiter(request.getParameter("withdrawAmount"));
            String username = getUsernameFromCookie(request);
            boolean isMatch = withdraw.matches("^[0-9]+$|^[0-9]+[.][0-9]{2}$");
            BigDecimal withdrawAmount = new BigDecimal(withdraw);

            amountValidator.validate(withdrawAmount);

            if (isMatch) {
                boolean isSuccessful = bankAccount.withdraw(withdrawAmount, username);
                if (!isSuccessful) {
                    request.setAttribute("operationStatus", messages.insufficientCurrentBalance());
                } else {
                    request.setAttribute("operationStatus", messages.successfullyWithdrawnAmount());
                }
                request.getRequestDispatcher("/withdraw.jsp").forward(request, response);
            } else {
                request.setAttribute("operationStatus", messages.withdrawAmountWithTooManyDecimalPlacesError());
                request.getRequestDispatcher("/withdraw.jsp").forward(request, response);
            }
        } catch (IncorrectAmountValueException e) {
            request.setAttribute("operationStatus", messages.withdrawNegativeAmountError());
            request.getRequestDispatcher("/withdraw.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("operationStatus", messages.withdrawStringAmountError());
            request.getRequestDispatcher("/withdraw.jsp").forward(request, response);
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
