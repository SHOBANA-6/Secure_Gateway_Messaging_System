package com.securegateway.servlet.admin;

import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * ToggleOfficerServlet – toggles is_active flag for an officer account.
 * Mapping: /admin/toggle-officer
 */
@WebServlet(name = "ToggleOfficerServlet", urlPatterns = {"/admin/toggle-officer"})
public class ToggleOfficerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ToggleOfficerServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
            return;
        }

        try {
            int userId = Integer.parseInt(idParam.trim());
            boolean result = userService.toggleOfficerStatus(userId);
            if (result) {
                req.getSession().setAttribute("flashSuccess", "Officer status updated successfully.");
            } else {
                req.getSession().setAttribute("flashError", "Could not update officer status.");
            }
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("flashError", "Invalid officer ID.");
        } catch (Exception e) {
            log.error("Error toggling officer status: {}", e.getMessage(), e);
            req.getSession().setAttribute("flashError", "A system error occurred.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
    }
}
