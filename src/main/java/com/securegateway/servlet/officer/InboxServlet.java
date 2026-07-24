package com.securegateway.servlet.officer;

import com.securegateway.model.Message;
import com.securegateway.model.User;
import com.securegateway.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * InboxServlet – shows received messages; marks a message as read when viewed.
 * Mapping: /officer/inbox
 */
@WebServlet(name = "InboxServlet", urlPatterns = {"/officer/inbox"})
public class InboxServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(InboxServlet.class);
    private static final long serialVersionUID = 1L;

    private final MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedInUser");
        int userId = user.getUserId();

        // Optional: mark a message as read when its ID is passed
        String readIdParam = req.getParameter("readId");
        if (readIdParam != null && !readIdParam.isBlank()) {
            try {
                int readId = Integer.parseInt(readIdParam.trim());
                Message msg = messageService.findById(readId);
                if (msg != null && msg.getReceiverId() == userId) {
                    messageService.markRead(readId, userId);
                    req.setAttribute("viewMessage", msg);
                }
            } catch (Exception e) {
                log.warn("Could not mark message as read: {}", e.getMessage());
            }
        }

        try {
            req.setAttribute("messages", messageService.getInbox(userId));
            req.setAttribute("unreadCount", messageService.countUnread(userId));
        } catch (Exception e) {
            log.error("Error loading inbox for userId={}: {}", userId, e.getMessage(), e);
            req.setAttribute("error", "Could not load inbox. Please try again.");
        }
        req.getRequestDispatcher("/officer/inbox.jsp").forward(req, resp);
    }
}
