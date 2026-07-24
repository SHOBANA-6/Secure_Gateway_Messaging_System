package com.securegateway.servlet.admin;

import com.securegateway.dao.UserDAO;
import com.securegateway.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/compose")
public class AdminComposeServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<User> officers = userDAO.findAllActiveOfficers();

            request.setAttribute("officers", officers);

            request.getRequestDispatcher("/admin/compose.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(e);

        }
    }
}