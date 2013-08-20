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
public class RegisterServlet extends HttpServlet {
    private UserRepository userRepository;

    @Inject
    public RegisterServlet(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("usernameBox");
        String password = request.getParameter("passwordBox");

        if (userRepository.add(new User(username, password))) {
            request.getSession().setAttribute("username", username);
            response.sendRedirect(request.getContextPath() + "/successfulRegistration.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/failedRegistration.jsp");
        }
    }
}
