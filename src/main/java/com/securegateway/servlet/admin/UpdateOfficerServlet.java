package com.securegateway.servlet.admin;

import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * UpdateOfficerServlet – processes the edit-officer form POST.
 * Mapping: /admin/update-officer
 */
@WebServlet(name = "UpdateOfficerServlet", urlPatterns = {"/admin/update-officer"})
public class UpdateOfficerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(UpdateOfficerServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String idParam      = req.getParameter("userId");
        String fullName     = req.getParameter("fullName");
        String email        = req.getParameter("email");
        String department   = req.getParameter("department");
        String badgeNumber  = req.getParameter("badgeNumber");
        String isActiveStr  = req.getParameter("isActive");
        String newPassword  = req.getParameter("newPassword");

        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
            return;
        }

        int userId;
        try {
            userId = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
            return;
        }

        boolean isActive = "1".equals(isActiveStr) || "true".equals(isActiveStr);

        try {
            boolean updated = userService.updateOfficer(
                    userId, fullName, email, department, badgeNumber, isActive, newPassword);

            if (updated) {
                req.getSession().setAttribute("flashSuccess", "Officer record updated successfully.");
            } else {
                req.getSession().setAttribute("flashError", "No changes were saved. Please try again.");
            }
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");

        } catch (IllegalArgumentException e) {
            try {
                req.setAttribute("error", e.getMessage());
                req.setAttribute("officer", userService.findById(userId));
                req.getRequestDispatcher("/admin/edit-officer.jsp").forward(req, resp);
            } catch (Exception ex) {
                resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
            }
        } catch (Exception e) {
            log.error("Error updating officer id={}: {}", userId, e.getMessage(), e);
            req.getSession().setAttribute("flashError", "A system error occurred while updating the record.");
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
        }
    }
}
