package com.securegateway.servlet.officer;

import com.securegateway.model.Message;
import com.securegateway.model.User;
import com.securegateway.service.MessageService;
import com.securegateway.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;

/**
 * DownloadAttachmentServlet – streams an attachment to the browser after verifying ownership.
 * Only the sender or receiver of the message may download the file.
 * Mapping: /officer/download-attachment
 */
@WebServlet(name = "DownloadAttachmentServlet", urlPatterns = {"/officer/download-attachment"})
public class DownloadAttachmentServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DownloadAttachmentServlet.class);
    private static final long serialVersionUID = 1L;
    private static final int BUFFER_SIZE = 8192;

    private final MessageService messageService = new MessageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String msgIdParam = req.getParameter("messageId");
        if (msgIdParam == null || msgIdParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Message ID is required.");
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loggedInUser");
        int userId = user.getUserId();

        try {
            int messageId = Integer.parseInt(msgIdParam.trim());
            Message msg   = messageService.findById(messageId);

            if (msg == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Message not found.");
                return;
            }
            // Security: only sender or receiver may download
            if (msg.getSenderId() != userId && msg.getReceiverId() != userId) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
                return;
            }
            if (!msg.hasAttachment()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No attachment for this message.");
                return;
            }

            String uploadDir  = FileUtil.resolveUploadDir(
                    getServletContext().getRealPath("/uploads"));
            Path filePath     = Paths.get(uploadDir, msg.getAttachmentPath()).normalize();
            Path baseDir      = Paths.get(uploadDir).normalize();

            // Prevent path traversal
            if (!filePath.startsWith(baseDir)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid file path.");
                return;
            }

            File file = filePath.toFile();
            if (!file.exists() || !file.isFile()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Attachment file not found on server.");
                return;
            }

            String mimeType = FileUtil.getMimeType(file.getName());
            resp.setContentType(mimeType);
            resp.setContentLengthLong(file.length());
            resp.setHeader("Content-Disposition",
                    "attachment; filename=\"" + msg.getAttachmentName() + "\"");
            resp.setHeader("Cache-Control", "no-cache");

            try (InputStream in  = new FileInputStream(file);
                 OutputStream out = resp.getOutputStream()) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            log.info("Attachment downloaded: messageId={} by userId={}", messageId, userId);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid message ID.");
        } catch (Exception e) {
            log.error("Error downloading attachment: {}", e.getMessage(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Download failed.");
        }
    }
}
