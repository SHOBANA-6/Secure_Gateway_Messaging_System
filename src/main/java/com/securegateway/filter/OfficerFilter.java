package com.securegateway.filter;

import com.securegateway.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * OfficerFilter – verifies the authenticated user has the OFFICER role.
 * Mapping: /officer/*
 */
@WebFilter(filterName = "OfficerFilter", urlPatterns = {"/officer/*"})
public class OfficerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

        if (user == null || !user.isOfficer()) {
            if (user != null && user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
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
