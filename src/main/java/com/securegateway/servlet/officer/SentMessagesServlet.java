package com.securegateway.servlet.officer;

import com.securegateway.model.User;
import com.securegateway.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * SentMessagesServlet – shows messages sent by the current officer.
 * Mapping: /officer/sent
 */
@WebServlet(name = "SentMessagesServlet", urlPatterns = {"/officer/sent"})
public class SentMessagesServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(SentMessagesServlet.class);
    private static final long serialVersionUID = 1L;

    private final MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user  = (User) session.getAttribute("loggedInUser");
        int userId = user.getUserId();

        try {
            req.setAttribute("messages", messageService.getSent(userId));

            String flash = (String) session.getAttribute("flashSuccess");
            if (flash != null) {
                req.setAttribute("flashSuccess", flash);
                session.removeAttribute("flashSuccess");
            }
        } catch (Exception e) {
            log.error("Error loading sent messages for userId={}: {}", userId, e.getMessage(), e);
            req.setAttribute("error", "Could not load sent messages.");
        }
        req.getRequestDispatcher("/officer/sent.jsp").forward(req, resp);
    }
}
