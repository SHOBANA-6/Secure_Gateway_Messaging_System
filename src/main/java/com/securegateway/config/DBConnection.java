package com.securegateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection – JDBC connection factory.
 * Loads driver once via static initialiser; provides per-request connections.
 *
 * Configure the four constants or override via system properties / JNDI for production.
 */
public final class DBConnection {

    private static final Logger log = LoggerFactory.getLogger(DBConnection.class);

    // ── Connection parameters ────────────────────────────────────────────────
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/secure_gateway_db"
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
            + "&characterEncoding=UTF-8&useUnicode=true";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "root";          // change to your MySQL password
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_CLASS);
            log.info("MySQL JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            log.error("MySQL JDBC driver not found on classpath: {}", e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() { /* utility class – no instances */ }

    /**
     * Returns a new JDBC {@link Connection} to secure_gateway_db.
     * Caller is responsible for closing it (preferably via try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
