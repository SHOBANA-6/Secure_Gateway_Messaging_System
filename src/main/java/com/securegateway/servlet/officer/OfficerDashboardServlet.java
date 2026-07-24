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
 * OfficerDashboardServlet – loads message stats and forwards to officer dashboard JSP.
 * Mapping: /officer/dashboard
 */
@WebServlet(name = "OfficerDashboardServlet", urlPatterns = {"/officer/dashboard"})
public class OfficerDashboardServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(OfficerDashboardServlet.class);
    private static final long serialVersionUID = 1L;

    private final MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("loggedInUser");

//        try {
//
//            req.setAttribute("unreadCount", 0);
//            req.setAttribute("inboxCount", 0);
//            req.setAttribute("sentCount", 0);
//            req.setAttribute("recentMessages", new java.util.ArrayList<>());
//
//            String flash = (String) session.getAttribute("flashSuccess");
//            if (flash != null) {
//                req.setAttribute("flashSuccess", flash);
//                session.removeAttribute("flashSuccess");
//            }
//
//        } catch (Exception e) {
//
//            log.error("Dashboard Error", e);
//            req.setAttribute("dashError", "Could not load dashboard.");
//
//        }

        try {

            int userId = user.getUserId();

            req.setAttribute("unreadCount", messageService.countUnread(userId));
            req.setAttribute("inboxCount", messageService.countInbox(userId));
            req.setAttribute("sentCount", messageService.countSent(userId));

            java.util.List<com.securegateway.model.Message> inbox =
                    messageService.getInbox(userId);

            if (inbox.size() > 5) {
                inbox = inbox.subList(0, 5);
            }

            req.setAttribute("recentMessages", inbox);

            String flash = (String) session.getAttribute("flashSuccess");
            if (flash != null) {
                req.setAttribute("flashSuccess", flash);
                session.removeAttribute("flashSuccess");
            }

        } catch (Exception e) {

            log.error("Dashboard Error", e);
            req.setAttribute("dashError", "Could not load dashboard.");

        }

        req.getRequestDispatcher("/officer/dashboard.jsp").forward(req, resp);
    }
}