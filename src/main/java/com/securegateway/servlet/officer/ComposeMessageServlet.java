package com.securegateway.servlet.officer;

import com.securegateway.model.User;
import com.securegateway.service.MessageService;
import com.securegateway.service.UserService;
import com.securegateway.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * ComposeMessageServlet – GET shows compose form; POST handles file upload + send.
 * Mapping: /officer/compose
 */
@WebServlet(name = "ComposeMessageServlet", urlPatterns = {"/officer/compose"})
public class ComposeMessageServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ComposeMessageServlet.class);
    private static final long serialVersionUID = 1L;

    private final UserService    userService    = new UserService();
    private final MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("activeOfficers", userService.getAllActiveOfficers());
        } catch (Exception e) {
            log.error("Error loading active officers: {}", e.getMessage(), e);
            req.setAttribute("error", "Could not load recipient list.");
        }
        req.getRequestDispatcher("/officer/compose.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        if (!ServletFileUpload.isMultipartContent(req)) {
            req.setAttribute("error", "Invalid form submission.");
            req.getRequestDispatcher("/officer/compose.jsp").forward(req, resp);
            return;
        }

        HttpSession session  = req.getSession(false);
        User sender          = (User) session.getAttribute("loggedInUser");

        String receiverIdStr = null;
        String subject       = null;
        String body          = null;
        String priority      = "NORMAL";
        String attachName    = null;
        String attachPath    = null;

        String uploadDir = FileUtil.resolveUploadDir(
                getServletContext().getRealPath("/uploads"));

        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024 * 8);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(FileUtil.MAX_FILE_SIZE);

            List<FileItem> items = upload.parseRequest(req);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        case "receiverId" -> receiverIdStr = item.getString("UTF-8");
                        case "subject"    -> subject       = item.getString("UTF-8");
                        case "body"       -> body          = item.getString("UTF-8");
                        case "priority"   -> priority      = item.getString("UTF-8");
                    }
                } else if ("attachment".equals(item.getFieldName()) && item.getSize() > 0) {
                    attachName = FileUtil.sanitizeFileName(item.getName());
                    attachPath = FileUtil.saveUploadedFile(item, uploadDir);
                }
            }

            if (receiverIdStr == null || receiverIdStr.isBlank()) {
                throw new IllegalArgumentException("Please select a recipient.");
            }
            int receiverId = Integer.parseInt(receiverIdStr.trim());

            int msgId = messageService.sendMessage(
                    sender.getUserId(), receiverId, subject, body, priority, attachName, attachPath);

            if (msgId > 0) {
                session.setAttribute("flashSuccess", "Message sent successfully.");
                resp.sendRedirect(req.getContextPath() + "/officer/sent");
            } else {
                req.setAttribute("error", "Message could not be sent. Please try again.");
                req.setAttribute("activeOfficers", userService.getAllActiveOfficers());
                req.getRequestDispatcher("/officer/compose.jsp").forward(req, resp);
            }

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            try { req.setAttribute("activeOfficers", userService.getAllActiveOfficers()); } catch (Exception ignored) {}
            req.getRequestDispatcher("/officer/compose.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error composing message: {}", e.getMessage(), e);
            req.setAttribute("error", "A system error occurred while sending the message.");
            try { req.setAttribute("activeOfficers", userService.getAllActiveOfficers()); } catch (Exception ignored) {}
            req.getRequestDispatcher("/officer/compose.jsp").forward(req, resp);
        }
    }
}
