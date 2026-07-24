package com.securegateway.model;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model for the {@code messages} table.
 * Column mapping:
 *   message_id | sender_id | receiver_id | subject | body |
 *   priority | is_read | attachment_name | attachment_path | sent_at
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Priority { LOW, NORMAL, HIGH, CRITICAL }

    private int           messageId;
    private int           senderId;
    private int           receiverId;
    private String        subject;
    private String        body;
    private Priority      priority;
    private boolean       isRead;
    private String        attachmentName;
    private String        attachmentPath;
    private LocalDateTime sentAt;

    // Joined fields (not stored in messages table)
    private String senderFullName;
    private String senderUsername;
    private String receiverFullName;
    private String receiverUsername;

    public Message() {}

    // ── Getters & Setters ────────────────────────────────────────────────────

    public int getMessageId()                          { return messageId; }
    public void setMessageId(int messageId)            { this.messageId = messageId; }

    public int getSenderId()                           { return senderId; }
    public void setSenderId(int senderId)              { this.senderId = senderId; }

    public int getReceiverId()                         { return receiverId; }
    public void setReceiverId(int receiverId)          { this.receiverId = receiverId; }

    public String getSubject()                         { return subject; }
    public void   setSubject(String subject)           { this.subject = subject; }

    public String getBody()                            { return body; }
    public void   setBody(String body)                 { this.body = body; }

    public Priority getPriority()                      { return priority; }
    public void     setPriority(Priority priority)     { this.priority = priority; }

    public void setPriorityFromString(String p) {
        try {
            this.priority = Priority.valueOf(p.toUpperCase());
        } catch (Exception e) {
            this.priority = Priority.NORMAL;
        }
    }

    public boolean isRead()                            { return isRead; }
    public void    setRead(boolean read)               { isRead = read; }

    public String getAttachmentName()                              { return attachmentName; }
    public void   setAttachmentName(String attachmentName)         { this.attachmentName = attachmentName; }

    public String getAttachmentPath()                              { return attachmentPath; }
    public void   setAttachmentPath(String attachmentPath)         { this.attachmentPath = attachmentPath; }

    public LocalDateTime getSentAt()                               { return sentAt; }
    public void          setSentAt(LocalDateTime sentAt)           { this.sentAt = sentAt; }

    public boolean hasAttachment() {
        return attachmentPath != null && !attachmentPath.isBlank();
    }

    // ── Joined getters / setters ──────────────────────────────────────────────

    public String getSenderFullName()                              { return senderFullName; }
    public void   setSenderFullName(String senderFullName)         { this.senderFullName = senderFullName; }

    public String getSenderUsername()                              { return senderUsername; }
    public void   setSenderUsername(String senderUsername)         { this.senderUsername = senderUsername; }

    public String getReceiverFullName()                            { return receiverFullName; }
    public void   setReceiverFullName(String receiverFullName)     { this.receiverFullName = receiverFullName; }

    public String getReceiverUsername()                            { return receiverUsername; }
    public void   setReceiverUsername(String receiverUsername)     { this.receiverUsername = receiverUsername; }

    @Override
    public String toString() {
        return "Message{messageId=" + messageId + ", subject='" + subject
                + "', priority=" + priority + ", isRead=" + isRead + "}";
    }
    public String getFormattedSentAt() {

        if (sentAt == null) {
            return "";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

        return sentAt.format(formatter);
    }
}
