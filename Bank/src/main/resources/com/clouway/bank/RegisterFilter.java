package com.clouway.bank;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author Grisha Angelov <grisha.angelov@clouway.com>
 */

@Singleton
public class RegisterFilter implements Filter {
    private CredentialsValidator credentialsValidator;

    @Inject
    public RegisterFilter(CredentialsValidator credentialsValidator) {
        this.credentialsValidator = credentialsValidator;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        String username = request.getParameter("usernameBox");
        String password = request.getParameter("passwordBox");

        if (credentialsValidator.isValid(new User(username, password))) {
            chain.doFilter(request, response);
        } else {
            request.setAttribute("message", "Please enter username and/or password");
            request.getRequestDispatcher("/registration.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {

    }

}
