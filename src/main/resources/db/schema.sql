-- ============================================================
-- SecureGateway – Secure Messaging & Communication System
-- Database Schema and Seed Data
-- MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS secure_gateway_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE secure_gateway_db;

-- ============================================================
-- TABLE: roles
-- ============================================================
CREATE TABLE IF NOT EXISTS roles (
    role_id   INT          NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(50)  NOT NULL,
    PRIMARY KEY (role_id),
    UNIQUE KEY uq_role_name (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: users
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    user_id       INT           NOT NULL AUTO_INCREMENT,
    username      VARCHAR(50)   NOT NULL,
    password_hash VARCHAR(255)  NOT NULL,
    full_name     VARCHAR(150)  NOT NULL,
    email         VARCHAR(150)  NOT NULL,
    department    VARCHAR(100)  DEFAULT NULL,
    badge_number  VARCHAR(50)   DEFAULT NULL,
    role_id       INT           NOT NULL,
    is_active     TINYINT(1)    NOT NULL DEFAULT 1,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    UNIQUE KEY uq_username (username),
    UNIQUE KEY uq_email (email),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_users_role_id (role_id),
    INDEX idx_users_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: messages
-- ============================================================
CREATE TABLE IF NOT EXISTS messages (
    message_id        INT           NOT NULL AUTO_INCREMENT,
    sender_id         INT           NOT NULL,
    receiver_id       INT           NOT NULL,
    subject           VARCHAR(255)  NOT NULL,
    body              TEXT          NOT NULL,
    priority          ENUM('LOW','NORMAL','HIGH','CRITICAL') NOT NULL DEFAULT 'NORMAL',
    is_read           TINYINT(1)    NOT NULL DEFAULT 0,
    attachment_name   VARCHAR(255)  DEFAULT NULL,
    attachment_path   VARCHAR(500)  DEFAULT NULL,
    sent_at           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (message_id),
    CONSTRAINT fk_messages_sender   FOREIGN KEY (sender_id)   REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_messages_receiver FOREIGN KEY (receiver_id) REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_messages_sender_id   (sender_id),
    INDEX idx_messages_receiver_id (receiver_id),
    INDEX idx_messages_is_read     (is_read),
    INDEX idx_messages_sent_at     (sent_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- SEED DATA: roles
-- ============================================================
INSERT INTO roles (role_id, role_name) VALUES
    (1, 'ADMIN'),
    (2, 'OFFICER')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- ============================================================
-- SEED DATA: users
-- BCrypt hashes generated with cost factor 10
-- Admin@123   -> $2a$10$2j/FzTVZXcJ1pAaW0Xchp.XrHDaD06IF9p67m1ppOgqidmr9ngzl6
-- Officer@123 -> $2a$10$ncGKjxYnQYAyUaVZapM7JeHIE9D37BoMcdx6gqYAzfiq6q7xySqzq
-- ============================================================
INSERT INTO users (user_id, username, password_hash, full_name, email, department, badge_number, role_id, is_active) VALUES
(1, 'admin',
 '$2a$10$2j/FzTVZXcJ1pAaW0Xchp.XrHDaD06IF9p67m1ppOgqidmr9ngzl6',
 'System Administrator', 'admin@securegateway.gov', 'IT Administration', 'ADM-001', 1, 1),
(2, 'jcarter',
 '$2a$10$ncGKjxYnQYAyUaVZapM7JeHIE9D37BoMcdx6gqYAzfiq6q7xySqzq',
 'James Carter', 'jcarter@securegateway.gov', 'Field Operations', 'OFC-101', 2, 1),
(3, 'mwilson',
 '$2a$10$ncGKjxYnQYAyUaVZapM7JeHIE9D37BoMcdx6gqYAzfiq6q7xySqzq',
 'Maria Wilson', 'mwilson@securegateway.gov', 'Intelligence', 'OFC-102', 2, 1),
(4, 'rthompson',
 '$2a$10$ncGKjxYnQYAyUaVZapM7JeHIE9D37BoMcdx6gqYAzfiq6q7xySqzq',
 'Robert Thompson', 'rthompson@securegateway.gov', 'Cybersecurity', 'OFC-103', 2, 1),
(5, 'llevy',
 '$2a$10$ncGKjxYnQYAyUaVZapM7JeHIE9D37BoMcdx6gqYAzfiq6q7xySqzq',
 'Linda Levy', 'llevy@securegateway.gov', 'Communications', 'OFC-104', 2, 0)
ON DUPLICATE KEY UPDATE
    password_hash = VALUES(password_hash),
    full_name     = VALUES(full_name),
    email         = VALUES(email),
    department    = VALUES(department),
    badge_number  = VALUES(badge_number),
    role_id       = VALUES(role_id),
    is_active     = VALUES(is_active);

-- ============================================================
-- SEED DATA: messages
-- ============================================================
INSERT INTO messages (message_id, sender_id, receiver_id, subject, body, priority, is_read, attachment_name, attachment_path, sent_at) VALUES
(1, 1, 2, 'Welcome to SecureGateway',
 'Welcome Officer Carter. Your account has been provisioned in the SecureGateway system. Please review your access permissions and report any discrepancies to the system administrator immediately.',
 'HIGH', 1, NULL, NULL, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(2, 2, 1, 'Field Report – Sector 7 Surveillance',
 'Reporting completed surveillance sweep of Sector 7 as assigned. All clear. No anomalies detected. Full report attached.',
 'NORMAL', 1, NULL, NULL, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(3, 1, 3, 'Intelligence Briefing Schedule',
 'Your quarterly intelligence briefing has been scheduled for next Monday at 09:00 hrs. Location: Conference Room B. Attendance mandatory.',
 'CRITICAL', 0, NULL, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(4, 3, 2, 'Data Sharing Request',
 'Requesting access to the field operation data for cross-department analysis. Please confirm availability for a coordination call.',
 'NORMAL', 0, NULL, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(5, 2, 3, 'Coordination Confirmed',
 'Confirmed for the coordination call. I will share the relevant datasets before the meeting.',
 'LOW', 0, NULL, NULL, NOW())
ON DUPLICATE KEY UPDATE
    sender_id      = VALUES(sender_id),
    receiver_id    = VALUES(receiver_id),
    subject        = VALUES(subject),
    body           = VALUES(body),
    priority       = VALUES(priority),
    is_read        = VALUES(is_read);
