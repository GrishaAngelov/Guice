package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
* @author Grisha Angelov <grisha.angelov@clouway.com>
*/
public class IndexServletTest {

    @Test
    public void forwardToIndexPage() throws Exception {
        Mockery context = new Mockery();
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        final HttpServletResponse response = context.mock(HttpServletResponse.class);
        IndexServlet indexServlet = new IndexServlet();

        context.checking(new Expectations(){{
            oneOf(request).getAttribute("greet");
            oneOf(request).getRequestDispatcher("/index.jsp");
        }});

        indexServlet.doGet(request, response);
        context.assertIsSatisfied();
    }
}
