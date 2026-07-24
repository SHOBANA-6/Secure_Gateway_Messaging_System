package com.securegateway.filter;

import com.securegateway.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * AdminFilter – verifies the authenticated user has the ADMIN role.
 * Mapping: /admin/*
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (user == null || !user.isAdmin()) {
            // Not admin: redirect to appropriate location
            if (user != null && user.isOfficer()) {
                resp.sendRedirect(req.getContextPath() + "/officer/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/login");
            }
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
