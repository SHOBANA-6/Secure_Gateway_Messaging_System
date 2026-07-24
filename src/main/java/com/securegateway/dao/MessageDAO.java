package com.securegateway.dao;

import com.securegateway.config.DBConnection;
import com.securegateway.model.Message;
import com.securegateway.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MessageDAO – all JDBC operations for the {@code messages} table.
 */
public class MessageDAO {

    private static final Logger log = LoggerFactory.getLogger(MessageDAO.class);

    private static final String COLS =
            "m.message_id, m.sender_id, m.receiver_id, m.subject, m.body, " +
            "m.priority, m.is_read, m.attachment_name, m.attachment_path, m.sent_at, " +
            "s.full_name AS sender_full_name, s.username AS sender_username, " +
            "r.full_name AS receiver_full_name, r.username AS receiver_username";

    private static final String JOINS =
            "FROM messages m " +
            "JOIN users s ON m.sender_id   = s.user_id " +
            "JOIN users r ON m.receiver_id = r.user_id ";

    private static final String SQL_FIND_BY_ID =
            "SELECT " + COLS + " " + JOINS + "WHERE m.message_id = ?";

    private static final String SQL_INBOX =
            "SELECT " + COLS + " " + JOINS +
            "WHERE m.receiver_id = ? ORDER BY m.sent_at DESC";

    private static final String SQL_SENT =
            "SELECT " + COLS + " " + JOINS +
            "WHERE m.sender_id = ? ORDER BY m.sent_at DESC";

    private static final String SQL_INSERT =
            "INSERT INTO messages (sender_id, receiver_id, subject, body, priority, " +
            "attachment_name, attachment_path) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_MARK_READ =
            "UPDATE messages SET is_read = 1 WHERE message_id = ? AND receiver_id = ?";

    private static final String SQL_COUNT_UNREAD =
            "SELECT COUNT(*) FROM messages WHERE receiver_id = ? AND is_read = 0";

    private static final String SQL_COUNT_INBOX =
            "SELECT COUNT(*) FROM messages WHERE receiver_id = ?";

    private static final String SQL_COUNT_SENT =
            "SELECT COUNT(*) FROM messages WHERE sender_id = ?";

    private static final String SQL_COUNT_TOTAL =
            "SELECT COUNT(*) FROM messages";

    // ── Read ─────────────────────────────────────────────────────────────────

    public Message findById(int messageId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, messageId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<Message> findInbox(int userId) throws SQLException {
        List<Message> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INBOX)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Message> findSent(int userId) throws SQLException {
        List<Message> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SENT)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public int countUnread(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_UNREAD)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int countInbox(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_INBOX)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int countSent(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_SENT)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int countTotal() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_TOTAL);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ── Write ────────────────────────────────────────────────────────────────

    public int insert(Message message) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getReceiverId());
//            ps.setString(3, message.getSubject());
//            ps.setString(4, message.getBody());
            ps.setString(3, com.securegateway.util.AESUtil.encrypt(message.getSubject()));
            ps.setString(4, com.securegateway.util.AESUtil.encrypt(message.getBody()));
            ps.setString(5, message.getPriority() != null
                    ? message.getPriority().name() : Message.Priority.NORMAL.name());
            ps.setString(6, message.getAttachmentName());
            ps.setString(7, message.getAttachmentPath());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
            return -1;
        }
    }

    public boolean markRead(int messageId, int receiverId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_MARK_READ)) {
            ps.setInt(1, messageId);
            ps.setInt(2, receiverId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Mapping ──────────────────────────────────────────────────────────────

    private Message mapRow(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setMessageId(rs.getInt("message_id"));
        m.setSenderId(rs.getInt("sender_id"));
        m.setReceiverId(rs.getInt("receiver_id"));
//        m.setSubject(rs.getString("subject"));
//        m.setBody(rs.getString("body"));
        m.setSubject(
                com.securegateway.util.AESUtil.decrypt(
                        rs.getString("subject"))
        );

        m.setBody(
                com.securegateway.util.AESUtil.decrypt(
                        rs.getString("body"))
        );
        m.setPriorityFromString(rs.getString("priority"));
        m.setRead(rs.getBoolean("is_read"));
        m.setAttachmentName(rs.getString("attachment_name"));
        m.setAttachmentPath(rs.getString("attachment_path"));
        Timestamp ts = rs.getTimestamp("sent_at");
        if (ts != null) m.setSentAt(ts.toLocalDateTime());
        m.setSenderFullName(rs.getString("sender_full_name"));
        m.setSenderUsername(rs.getString("sender_username"));
        m.setReceiverFullName(rs.getString("receiver_full_name"));
        m.setReceiverUsername(rs.getString("receiver_username"));
        return m;
    }


    public boolean sendMessage(int senderId,
                               int receiverId,
                               String subject,
                               String body,
                               String attachmentPath)
            throws SQLException {


        Message message = new Message();

        message.setSenderId(senderId);
        message.setReceiverId(receiverId);

        message.setSubject(subject);
        message.setBody(body);

        message.setPriority(Message.Priority.NORMAL);

        message.setAttachmentPath(attachmentPath);


        return insert(message) > 0;
    }

}
