package com.securegateway.servlet.admin;

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AdminSendMessageServlet",
        urlPatterns = {"/admin/send-message"})
public class AdminSendMessageServlet extends HttpServlet {

    private static final Logger log =
            LoggerFactory.getLogger(AdminSendMessageServlet.class);

    private final UserService userService = new UserService();
    private final MessageService messageService = new MessageService();

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        if (!ServletFileUpload.isMultipartContent(req)) {

            req.setAttribute("error", "Invalid Request");
            try {
                req.setAttribute("officers",
                        userService.getAllActiveOfficers());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            req.getRequestDispatcher("/admin/compose.jsp")
                    .forward(req, resp);

            return;
        }

        HttpSession session = req.getSession(false);

        User sender =
                (User) session.getAttribute("loggedInUser");

        List<Integer> receiverIds = new ArrayList<>();

        String subject = null;
        String body = null;
        String priority = "NORMAL";

        String attachmentName = null;
        String attachmentPath = null;

        String uploadDir =
                FileUtil.resolveUploadDir(
                        getServletContext()
                                .getRealPath("/uploads"));

        try {

            DiskFileItemFactory factory =
                    new DiskFileItemFactory();

            factory.setSizeThreshold(1024 * 8);

            ServletFileUpload upload =
                    new ServletFileUpload(factory);

            upload.setSizeMax(FileUtil.MAX_FILE_SIZE);

            List<FileItem> items =
                    upload.parseRequest(req);

            for (FileItem item : items) {

                if (item.isFormField()) {

                    switch (item.getFieldName()) {

                        case "receiverIds":

                            receiverIds.add(
                                    Integer.parseInt(
                                            item.getString("UTF-8")
                                    )
                            );

                            break;

                        case "subject":

                            subject =
                                    item.getString("UTF-8");

                            break;

                        case "message":

                            body =
                                    item.getString("UTF-8");

                            break;

                        case "priority":

                            priority =
                                    item.getString("UTF-8");

                            break;
                    }

                }

                else if ("attachment".equals(item.getFieldName())
                        && item.getSize() > 0) {

                    attachmentName =
                            FileUtil.sanitizeFileName(
                                    item.getName());

                    attachmentPath =
                            FileUtil.saveUploadedFile(
                                    item,
                                    uploadDir);

                }

            }

            if(receiverIds.isEmpty()){

                throw new IllegalArgumentException(
                        "Select at least one officer."
                );

            }

            for(Integer receiverId : receiverIds){

                messageService.sendMessage(

                        sender.getUserId(),

                        receiverId,

                        subject,

                        body,

                        priority,

                        attachmentName,

                        attachmentPath

                );

            }

            session.setAttribute(
                    "flashSuccess",
                    "Message sent successfully."
            );

            resp.sendRedirect(
                    req.getContextPath()
                            + "/admin/sent");

        }

        catch (Exception e) {

            log.error(e.getMessage(), e);

            req.setAttribute(
                    "error",
                    e.getMessage());

            try{

                req.setAttribute(
                        "officers",
                        userService.getAllActiveOfficers());

            }catch(Exception ignored){}

            req.getRequestDispatcher("/admin/compose.jsp")
                    .forward(req, resp);

        }

    }

}