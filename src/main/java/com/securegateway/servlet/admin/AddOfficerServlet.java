package com.securegateway.servlet.admin;

import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * AddOfficerServlet – GET shows the form; POST processes the new officer creation.
 * Mapping: /admin/add-officer
 */
@WebServlet(name = "AddOfficerServlet", urlPatterns = {"/admin/add-officer"})
public class AddOfficerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AddOfficerServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/admin/add-officer.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String username    = req.getParameter("username");
        String password    = req.getParameter("password");
        String fullName    = req.getParameter("fullName");
        String email       = req.getParameter("email");
        String department  = req.getParameter("department");
        String badgeNumber = req.getParameter("badgeNumber");

        try {
            int newId = userService.addOfficer(username, password, fullName, email, department, badgeNumber);
            if (newId > 0) {
                req.getSession().setAttribute("flashSuccess", "Officer account created successfully.");
                resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
            } else {
                req.setAttribute("error", "Failed to create officer account. Please try again.");
                req.setAttribute("username",    username);
                req.setAttribute("fullName",    fullName);
                req.setAttribute("email",       email);
                req.setAttribute("department",  department);
                req.setAttribute("badgeNumber", badgeNumber);
                req.getRequestDispatcher("/admin/add-officer.jsp").forward(req, resp);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("username",    username);
            req.setAttribute("fullName",    fullName);
            req.setAttribute("email",       email);
            req.setAttribute("department",  department);
            req.setAttribute("badgeNumber", badgeNumber);
            req.getRequestDispatcher("/admin/add-officer.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error adding officer: {}", e.getMessage(), e);
            req.setAttribute("error", "A system error occurred. Please contact support.");
            req.getRequestDispatcher("/admin/add-officer.jsp").forward(req, resp);
        }
    }
}
