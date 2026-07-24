package com.securegateway.servlet.admin;

import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * ViewOfficersServlet – lists all officer accounts with flash message support.
 * Mapping: /admin/view-officers
 */
@WebServlet(name = "ViewOfficersServlet", urlPatterns = {"/admin/view-officers"})
public class ViewOfficersServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ViewOfficersServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("officers", userService.getAllOfficers());

            // Pull and clear flash message from session
            HttpSession session = req.getSession(false);
            if (session != null) {
                String flash = (String) session.getAttribute("flashSuccess");
                if (flash != null) {
                    req.setAttribute("flashSuccess", flash);
                    session.removeAttribute("flashSuccess");
                }
                String flashErr = (String) session.getAttribute("flashError");
                if (flashErr != null) {
                    req.setAttribute("flashError", flashErr);
                    session.removeAttribute("flashError");
                }
            }
        } catch (Exception e) {
            log.error("Error loading officers list: {}", e.getMessage(), e);
            req.setAttribute("error", "Could not load officer list. Please try again.");
        }
        req.getRequestDispatcher("/admin/view-officers.jsp").forward(req, resp);
    }
}
