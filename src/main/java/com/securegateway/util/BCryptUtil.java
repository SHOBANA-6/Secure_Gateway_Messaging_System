package com.securegateway.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * BCryptUtil – thin wrapper around jBCrypt for password hashing and verification.
 * Cost factor 12 for new hashes; verification works for any valid BCrypt hash.
 */
public final class BCryptUtil {

    private static final int COST_FACTOR = 12;

    private BCryptUtil() { /* utility class */ }

    /**
     * Hashes a plain-text password using BCrypt with cost factor {@value #COST_FACTOR}.
     *
     * @param plainPassword the raw password
     * @return BCrypt hash string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password must not be blank.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(COST_FACTOR));
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     * Handles both $2a$ and $2b$ prefixes transparently.
     *
     * @param plainPassword the candidate password
     * @param storedHash    the hash retrieved from the database
     * @return {@code true} if they match
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        // Normalise $2b$ → $2a$ for jBCrypt compatibility
        String normalizedHash = storedHash.startsWith("$2b$")
                ? "$2a$" + storedHash.substring(4)
                : storedHash;
        try {
            return BCrypt.checkpw(plainPassword, normalizedHash);
        } catch (Exception e) {
            return false;
        }
    }
}
