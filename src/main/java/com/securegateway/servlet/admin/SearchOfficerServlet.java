package com.securegateway.servlet.admin;

import com.securegateway.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * SearchOfficerServlet – searches officer accounts and renders results via view-officers.jsp.
 * Mapping: /admin/search-officers
 */
@WebServlet(name = "SearchOfficerServlet", urlPatterns = {"/admin/search-officers"})
public class SearchOfficerServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SearchOfficerServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        try {
            req.setAttribute("officers", userService.searchOfficers(keyword));
            req.setAttribute("searchKeyword", keyword != null ? keyword : "");
        } catch (Exception e) {
            log.error("Error searching officers: {}", e.getMessage(), e);
            req.setAttribute("error", "Search failed. Please try again.");
        }
        req.getRequestDispatcher("/admin/view-officers.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
