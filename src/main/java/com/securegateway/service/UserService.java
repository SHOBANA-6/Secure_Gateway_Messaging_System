package com.securegateway.service;

import com.securegateway.dao.UserDAO;
import com.securegateway.model.User;
import com.securegateway.util.BCryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * UserService – business logic layer for user management and authentication.
 * All DAO calls are wrapped here; servlets only interact with this service.
 */
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO = new UserDAO();

    // ── Authentication ───────────────────────────────────────────────────────

    /**
     * Authenticates a user by username and plain-text password.
     *
     * @return the authenticated {@link User} if credentials are valid and account is active,
     *         or {@code null} otherwise.
     */
    public User authenticate(String username, String plainPassword) {
        if (username == null || username.isBlank() || plainPassword == null || plainPassword.isBlank()) {
            return null;
        }
        try {
            User user = userDAO.findByUsername(username.trim().toLowerCase());
            if (user == null) {
                log.warn("Login attempt for unknown username: {}", username);
                return null;
            }
            if (!user.isActive()) {
                log.warn("Login attempt for deactivated account: {}", username);
                return null;
            }
            if (!BCryptUtil.verifyPassword(plainPassword, user.getPasswordHash())) {
                log.warn("Invalid password for username: {}", username);
                return null;
            }
            log.info("User authenticated: {} [{}]", username, user.getRoleName());
            return user;
        } catch (SQLException e) {
            log.error("SQLException during authentication for {}: {}", username, e.getMessage(), e);
            return null;
        }
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    public User findById(int userId) throws SQLException {
        return userDAO.findById(userId);
    }

    public User findByUsername(String username) throws SQLException {
        return userDAO.findByUsername(username);
    }

    public List<User> getAllOfficers() throws SQLException {
        return userDAO.findAllOfficers();
    }

    public List<User> getAllActiveOfficers() throws SQLException {
        return userDAO.findAllActiveOfficers();
    }

    public List<User> searchOfficers(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return userDAO.findAllOfficers();
        }
        return userDAO.searchOfficers(keyword.trim());
    }

    public int countOfficers() throws SQLException {
        return userDAO.countOfficers();
    }

    public int countActiveOfficers() throws SQLException {
        return userDAO.countActiveOfficers();
    }

    // ── Write ────────────────────────────────────────────────────────────────

    /**
     * Adds a new officer account.
     *
     * @return the generated user_id, or -1 on failure.
     * @throws IllegalArgumentException if validation fails (duplicate username / email).
     */
    public int addOfficer(String username, String plainPassword, String fullName,
                          String email, String department, String badgeNumber) throws SQLException {

        validateRequired(username, "Username");
        validateRequired(plainPassword, "Password");
        validateRequired(fullName, "Full Name");
        validateRequired(email, "Email");

        String normUsername = username.trim().toLowerCase();
        String normEmail    = email.trim().toLowerCase();

        if (userDAO.existsUsernameForOther(normUsername, -1)) {
            throw new IllegalArgumentException("Username '" + normUsername + "' is already taken.");
        }
        if (userDAO.existsEmailForOther(normEmail, -1)) {
            throw new IllegalArgumentException("Email '" + normEmail + "' is already registered.");
        }

        User user = new User();
        user.setUsername(normUsername);
        user.setPasswordHash(BCryptUtil.hashPassword(plainPassword));
        user.setFullName(fullName.trim());
        user.setEmail(normEmail);
        user.setDepartment(department != null ? department.trim() : null);
        user.setBadgeNumber(badgeNumber != null ? badgeNumber.trim() : null);
        user.setRoleId(2); // OFFICER role
        user.setActive(true);

        int newId = userDAO.insert(user);
        if (newId > 0) {
            log.info("Officer account created: {} (id={})", normUsername, newId);
        }
        return newId;
    }

    /**
     * Updates officer profile fields. Password is updated only when {@code newPlainPassword} is non-blank.
     */
    public boolean updateOfficer(int userId, String fullName, String email,
                                  String department, String badgeNumber,
                                  boolean isActive, String newPlainPassword) throws SQLException {

        validateRequired(fullName, "Full Name");
        validateRequired(email, "Email");

        String normEmail = email.trim().toLowerCase();
        if (userDAO.existsEmailForOther(normEmail, userId)) {
            throw new IllegalArgumentException("Email '" + normEmail + "' is already used by another account.");
        }

        User user = new User();
        user.setUserId(userId);
        user.setFullName(fullName.trim());
        user.setEmail(normEmail);
        user.setDepartment(department != null ? department.trim() : null);
        user.setBadgeNumber(badgeNumber != null ? badgeNumber.trim() : null);
        user.setRoleId(2);
        user.setActive(isActive);

        boolean updated = userDAO.update(user);

        if (updated && newPlainPassword != null && !newPlainPassword.isBlank()) {
            userDAO.updatePassword(userId, BCryptUtil.hashPassword(newPlainPassword));
            log.info("Password updated for user id={}", userId);
        }

        if (updated) log.info("Officer profile updated: id={}", userId);
        return updated;
    }

    public boolean toggleOfficerStatus(int userId) throws SQLException {
        boolean result = userDAO.toggleActive(userId);
        if (result) log.info("Toggled active status for user id={}", userId);
        return result;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void validateRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
    }
}
