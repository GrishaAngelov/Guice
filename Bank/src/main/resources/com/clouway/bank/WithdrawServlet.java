package com.clouway.bank;

import com.google.inject.Singleton;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
@Singleton
public class WithdrawServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    doPost(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
      request.getRequestDispatcher("/withdraw.jsp").forward(request, response);
  }
}
