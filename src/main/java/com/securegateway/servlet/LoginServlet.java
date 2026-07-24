package com.securegateway.servlet;

import com.securegateway.model.User;
import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet – handles GET (show login page) and POST (process credentials).
 * Mapping: /login
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // If already logged in, redirect to appropriate dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            redirectByRole((User) session.getAttribute("loggedInUser"), req, resp);
            return;
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String username     = req.getParameter("username");
        String password     = req.getParameter("password");
        String loginRole = req.getParameter("loginRole");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            req.setAttribute("error", "Username and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        User user = userService.authenticate(username.trim(), password);

        if (user == null) {
            log.warn("Failed login attempt for username: {}", username);
            req.setAttribute("error", "Invalid credentials or account is deactivated.");
            req.setAttribute("username", username);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        if ("ADMIN".equalsIgnoreCase(loginRole) && !user.isAdmin()) {

            req.setAttribute("error",
                    "If you are an Officer, please switch to the Officer tab to login.");

            req.setAttribute("username", username);

            req.getRequestDispatcher("/login.jsp").forward(req, resp);

            return;
        }

        if ("OFFICER".equalsIgnoreCase(loginRole) && user.isAdmin()) {

            req.setAttribute("error",
                    "If you are an Admin, please switch to the Admin tab to login.");

            req.setAttribute("username", username);

            req.getRequestDispatcher("/login.jsp").forward(req, resp);

            return;
        }

        // Invalidate any existing session to prevent session fixation
        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = req.getSession(true);
        session.setAttribute("loggedInUser", user);
        session.setAttribute("userId",       user.getUserId());
        session.setAttribute("username",     user.getUsername());
        session.setAttribute("fullName",     user.getFullName());
        session.setAttribute("roleName",     user.getRoleName());
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        log.info("User logged in: {} [{}]", user.getUsername(), user.getRoleName());
        redirectByRole(user, req, resp);
    }

    private void redirectByRole(User user, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String ctx = req.getContextPath();
        if (user.isAdmin()) {
            resp.sendRedirect(ctx + "/admin/dashboard");
        } else {
            resp.sendRedirect(ctx + "/officer/dashboard");
        }
    }
}
