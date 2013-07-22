package com.clouway.bank;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */
public class IndexServletTest {

    @Test
    public void forwardToIndexPage() throws Exception {
        IndexServlet indexServlet = new IndexServlet();
        Mockery context = new Mockery();
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        final HttpServletResponse response = context.mock(HttpServletResponse.class);

        context.checking(new Expectations(){{
            oneOf(request).getRequestDispatcher("/index.jsp");
        }});

        indexServlet.doGet(request, response);
        context.assertIsSatisfied();
    }
}
