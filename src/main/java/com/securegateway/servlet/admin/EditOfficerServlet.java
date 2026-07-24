package com.securegateway.servlet.admin;

import com.securegateway.model.User;
import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * EditOfficerServlet – loads an officer's data and forwards to the edit form.
 * Mapping: /admin/edit-officer
 */
@WebServlet(name = "EditOfficerServlet", urlPatterns = {"/admin/edit-officer"})
public class EditOfficerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(EditOfficerServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
            return;
        }
        try {
            int userId = Integer.parseInt(idParam.trim());
            User officer = userService.findById(userId);
            if (officer == null || officer.isAdmin()) {
                req.getSession().setAttribute("flashError", "Officer not found.");
                resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
                return;
            }
            req.setAttribute("officer", officer);
            req.getRequestDispatcher("/admin/edit-officer.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
        } catch (Exception e) {
            log.error("Error loading officer for edit: {}", e.getMessage(), e);
            req.getSession().setAttribute("flashError", "Could not load officer data.");
            resp.sendRedirect(req.getContextPath() + "/admin/view-officers");
        }
    }
}
