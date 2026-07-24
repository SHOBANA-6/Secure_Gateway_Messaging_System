package com.securegateway.dao;

import com.securegateway.config.DBConnection;
import com.securegateway.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO – all JDBC operations for the {@code users} table.
 * Uses PreparedStatement throughout to prevent SQL injection.
 */
public class UserDAO {

    private static final Logger log = LoggerFactory.getLogger(UserDAO.class);

    // ── SQL constants ────────────────────────────────────────────────────────
    private static final String SQL_FIND_BY_USERNAME =
            "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.email, " +
            "u.department, u.badge_number, u.role_id, r.role_name, u.is_active, " +
            "u.created_at, u.updated_at " +
            "FROM users u JOIN roles r ON u.role_id = r.role_id " +
            "WHERE u.username = ?";

    private static final String SQL_FIND_BY_ID =
            "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.email, " +
            "u.department, u.badge_number, u.role_id, r.role_name, u.is_active, " +
            "u.created_at, u.updated_at " +
            "FROM users u JOIN roles r ON u.role_id = r.role_id " +
            "WHERE u.user_id = ?";

    private static final String SQL_FIND_ALL_OFFICERS =
            "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.email, " +
            "u.department, u.badge_number, u.role_id, r.role_name, u.is_active, " +
            "u.created_at, u.updated_at " +
            "FROM users u JOIN roles r ON u.role_id = r.role_id " +
            "WHERE r.role_name = 'OFFICER' ORDER BY u.full_name ASC";

    private static final String SQL_FIND_ALL_ACTIVE_OFFICERS =
            "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.email, " +
            "u.department, u.badge_number, u.role_id, r.role_name, u.is_active, " +
            "u.created_at, u.updated_at " +
            "FROM users u JOIN roles r ON u.role_id = r.role_id " +
            "WHERE r.role_name = 'OFFICER' AND u.is_active = 1 ORDER BY u.full_name ASC";

    private static final String SQL_SEARCH_OFFICERS =
            "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.email, " +
            "u.department, u.badge_number, u.role_id, r.role_name, u.is_active, " +
            "u.created_at, u.updated_at " +
            "FROM users u JOIN roles r ON u.role_id = r.role_id " +
            "WHERE r.role_name = 'OFFICER' AND " +
            "(u.full_name LIKE ? OR u.username LIKE ? OR u.badge_number LIKE ? OR u.department LIKE ?) " +
            "ORDER BY u.full_name ASC";

    private static final String SQL_INSERT_USER =
            "INSERT INTO users (username, password_hash, full_name, email, department, badge_number, role_id, is_active) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, 1)";

    private static final String SQL_UPDATE_USER =
            "UPDATE users SET full_name=?, email=?, department=?, badge_number=?, " +
            "role_id=?, is_active=? WHERE user_id=?";

    private static final String SQL_UPDATE_PASSWORD =
            "UPDATE users SET password_hash=? WHERE user_id=?";

    private static final String SQL_TOGGLE_ACTIVE =
            "UPDATE users SET is_active = NOT is_active WHERE user_id=?";

    private static final String SQL_COUNT_OFFICERS =
            "SELECT COUNT(*) FROM users WHERE role_id = 2";

    private static final String SQL_COUNT_ACTIVE_OFFICERS =
            "SELECT COUNT(*) FROM users WHERE role_id = 2 AND is_active = 1";

    private static final String SQL_EXISTS_USERNAME =
            "SELECT 1 FROM users WHERE username = ? AND user_id != ?";

    private static final String SQL_EXISTS_EMAIL =
            "SELECT 1 FROM users WHERE email = ? AND user_id != ?";

    // ── Read ─────────────────────────────────────────────────────────────────

    public User findByUsername(String username) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_USERNAME)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public User findById(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_BY_ID)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<User> findAllOfficers() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL_OFFICERS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<User> findAllActiveOfficers() throws SQLException {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_FIND_ALL_ACTIVE_OFFICERS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<User> searchOfficers(String keyword) throws SQLException {
        List<User> list = new ArrayList<>();
        String pattern = "%" + keyword + "%";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SEARCH_OFFICERS)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public int countOfficers() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_OFFICERS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countActiveOfficers() throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_COUNT_ACTIVE_OFFICERS);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public boolean existsUsernameForOther(String username, int excludeUserId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_USERNAME)) {
            ps.setString(1, username);
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsEmailForOther(String email, int excludeUserId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_EXISTS_EMAIL)) {
            ps.setString(1, email);
            ps.setInt(2, excludeUserId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ── Write ────────────────────────────────────────────────────────────────

    public int insert(User user) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getDepartment());
            ps.setString(6, user.getBadgeNumber());
            ps.setInt(7, user.getRoleId());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
            return -1;
        }
    }

    public boolean update(User user) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_USER)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getDepartment());
            ps.setString(4, user.getBadgeNumber());
            ps.setInt(5, user.getRoleId());
            ps.setBoolean(6, user.isActive());
            ps.setInt(7, user.getUserId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePassword(int userId, String newHash) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PASSWORD)) {
            ps.setString(1, newHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean toggleActive(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_TOGGLE_ACTIVE)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Mapping ──────────────────────────────────────────────────────────────

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("user_id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setDepartment(rs.getString("department"));
        u.setBadgeNumber(rs.getString("badge_number"));
        u.setRoleId(rs.getInt("role_id"));
        u.setRoleName(rs.getString("role_name"));
        u.setActive(rs.getBoolean("is_active"));
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) u.setCreatedAt(ca.toLocalDateTime());
        Timestamp ua = rs.getTimestamp("updated_at");
        if (ua != null) u.setUpdatedAt(ua.toLocalDateTime());
        return u;
    }

}
