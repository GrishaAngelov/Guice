package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class DepositServletTest {

    @Test
    public void forwardToDepositPage() throws Exception {
        DepositServlet depositServlet = new DepositServlet();
        Mockery context = new Mockery();
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        HttpServletResponse response = context.mock(HttpServletResponse.class);

        context.checking(new Expectations(){{
            oneOf(request).getRequestDispatcher("/deposit.jsp");
        }});

        depositServlet.doPost(request, response);
        context.assertIsSatisfied();
    }
}
