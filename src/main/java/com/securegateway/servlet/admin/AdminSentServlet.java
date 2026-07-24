package com.securegateway.servlet.admin;

import com.securegateway.dao.MessageDAO;
import com.securegateway.model.Message;
import com.securegateway.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/sent")
public class AdminSentServlet extends HttpServlet {

    private final MessageDAO messageDAO = new MessageDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {

            User admin =
                    (User) request.getSession().getAttribute("loggedInUser");

            List<Message> sentMessages =
                    messageDAO.findSent(admin.getUserId());

            request.setAttribute("sentMessages", sentMessages);

            request.getRequestDispatcher("/admin/sent.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            throw new ServletException(e);

        }

    }
}