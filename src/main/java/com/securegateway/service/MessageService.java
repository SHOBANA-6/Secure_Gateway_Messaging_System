package com.securegateway.service;

import com.securegateway.dao.MessageDAO;
import com.securegateway.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * MessageService – business logic for secure message operations.
 */
public class MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);
    private final MessageDAO messageDAO = new MessageDAO();

    // ── Read ─────────────────────────────────────────────────────────────────

    public Message findById(int messageId) throws SQLException {
        return messageDAO.findById(messageId);
    }

    public List<Message> getInbox(int userId) throws SQLException {
        return messageDAO.findInbox(userId);
    }

    public List<Message> getSent(int userId) throws SQLException {
        return messageDAO.findSent(userId);
    }

    public int countUnread(int userId) throws SQLException {
        return messageDAO.countUnread(userId);
    }

    public int countInbox(int userId) throws SQLException {
        return messageDAO.countInbox(userId);
    }

    public int countSent(int userId) throws SQLException {
        return messageDAO.countSent(userId);
    }

    public int countTotal() throws SQLException {
        return messageDAO.countTotal();
    }

    // ── Write ────────────────────────────────────────────────────────────────

    /**
     * Sends a secure message. Validates required fields before persistence.
     *
     * @param senderId       user_id of the sender
     * @param receiverId     user_id of the intended receiver
     * @param subject        message subject (required)
     * @param body           message body (required)
     * @param priority       one of LOW/NORMAL/HIGH/CRITICAL (default NORMAL)
     * @param attachmentName original filename of attachment, or null
     * @param attachmentPath server-side path to saved file, or null
     * @return generated message_id, or -1 on failure
     */
    public int sendMessage(int senderId, int receiverId, String subject, String body,
                           String priority, String attachmentName, String attachmentPath)
            throws SQLException {

        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject is required.");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("Message body is required.");
        }
        if (senderId == receiverId) {
            throw new IllegalArgumentException("Cannot send a message to yourself.");
        }

        Message msg = new Message();
        msg.setSenderId(senderId);
        msg.setReceiverId(receiverId);
        msg.setSubject(subject.trim());
        msg.setBody(body.trim());
        msg.setPriorityFromString(priority != null ? priority : "NORMAL");
        msg.setAttachmentName(attachmentName);
        msg.setAttachmentPath(attachmentPath);

        int id = messageDAO.insert(msg);
        if (id > 0) {
            log.info("Message sent: id={} from sender={} to receiver={}", id, senderId, receiverId);
        }
        return id;
    }

    /**
     * Marks a message as read, verifying that the caller is indeed the receiver.
     */
    public boolean markRead(int messageId, int receiverId) throws SQLException {
        return messageDAO.markRead(messageId, receiverId);
    }
}
