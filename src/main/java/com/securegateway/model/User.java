package com.securegateway.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model for the {@code users} table.
 * Column mapping:
 *   user_id | username | password_hash | full_name | email |
 *   department | badge_number | role_id | is_active | created_at | updated_at
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private int           userId;
    private String        username;
    private String        passwordHash;
    private String        fullName;
    private String        email;
    private String        department;
    private String        badgeNumber;
    private int           roleId;
    private String        roleName;        // joined from roles table
    private boolean       isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    // ── Getters & Setters ────────────────────────────────────────────────────

    public int getUserId()                         { return userId; }
    public void setUserId(int userId)              { this.userId = userId; }

    public String getUsername()                    { return username; }
    public void   setUsername(String username)     { this.username = username; }

    public String getPasswordHash()                        { return passwordHash; }
    public void   setPasswordHash(String passwordHash)     { this.passwordHash = passwordHash; }

    public String getFullName()                    { return fullName; }
    public void   setFullName(String fullName)     { this.fullName = fullName; }

    public String getEmail()                       { return email; }
    public void   setEmail(String email)           { this.email = email; }

    public String getDepartment()                      { return department; }
    public void   setDepartment(String department)     { this.department = department; }

    public String getBadgeNumber()                         { return badgeNumber; }
    public void   setBadgeNumber(String badgeNumber)       { this.badgeNumber = badgeNumber; }

    public int  getRoleId()                  { return roleId; }
    public void setRoleId(int roleId)        { this.roleId = roleId; }

    public String getRoleName()                    { return roleName; }
    public void   setRoleName(String roleName)     { this.roleName = roleName; }

    public boolean isActive()                      { return isActive; }
    public void    setActive(boolean active)       { isActive = active; }

    public LocalDateTime getCreatedAt()                        { return createdAt; }
    public void          setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt()                        { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ── Convenience ──────────────────────────────────────────────────────────

    /** Returns true when this user has the ADMIN role. */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(roleName) || roleId == 1;
    }

    /** Returns true when this user has the OFFICER role. */
    public boolean isOfficer() {
        return "OFFICER".equalsIgnoreCase(roleName) || roleId == 2;
    }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username='" + username
                + "', fullName='" + fullName + "', roleName='" + roleName
                + "', isActive=" + isActive + "}";
    }
}
