package com.securegateway.servlet.admin;

import com.securegateway.service.MessageService;
import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * AdminDashboardServlet – loads statistics and forwards to the admin dashboard JSP.
 * Mapping: /admin/dashboard
 */
@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminDashboardServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService    userService    = new UserService();
    private final MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("totalOfficers",  userService.countOfficers());
            req.setAttribute("activeOfficers", userService.countActiveOfficers());
            req.setAttribute("inactiveOfficers",
                    userService.countOfficers() - userService.countActiveOfficers());
            req.setAttribute("totalMessages",  messageService.countTotal());
            req.setAttribute("recentOfficers", userService.getAllOfficers());
        } catch (Exception e) {
            log.error("Error loading admin dashboard data: {}", e.getMessage(), e);
            req.setAttribute("dashError", "Could not load statistics. Please try again.");
        }
        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
