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
public class WithdrawOperationServlet extends HttpServlet {
    private Account bankAccount;

    @Inject
    public WithdrawOperationServlet(Account bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void init() throws ServletException {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String withdrawAmount = checkDelimiter(request.getParameter("withdrawAmount"));
        String username = String.valueOf(request.getSession().getAttribute("username"));
        boolean isMatch = withdrawAmount.matches("^[0-9]+$|^[0-9]+[.][0-9]{2}$");

        try{
        if (isMatch) {
            boolean isSuccessful = bankAccount.withdraw(withdrawAmount, username);
            if (!isSuccessful) {
                request.getSession().setAttribute("operationStatus", "Can not perform operation! Not enough money in your account!");
            } else {
                request.getSession().setAttribute("operationStatus", "Amount successfully withdrawn!");
            }
            response.sendRedirect(request.getContextPath() + "/withdraw.jsp");
        } else {
            request.getSession().setAttribute("operationStatus", "Amount not withdrawn!");
            response.sendRedirect(request.getContextPath() + "/withdraw.jsp");
        }
        }catch (IncorrectAmountValueException e){
            request.getSession().setAttribute("operationStatus", "Amount not withdrawn!");
            response.sendRedirect(request.getContextPath() + "/withdraw.jsp");

        }
    }

    public void destroy() {

    }

    private String checkDelimiter(String amount) {
        if (amount.contains(",")) {
            amount = amount.replace(',', '.');
        }
        if(amount.contains(".") && amount.indexOf(".")+3!=amount.length()){
            amount = amount.concat("0");
        }
        return amount;
    }
}
