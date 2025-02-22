package com.example.demo.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Utility for managing database connections using HikariCP.
 */
public class DBConnectionUtils {

    // Connection pool configuration
    private static final HikariDataSource HIKARI_DATA_SOURCE;

    private DBConnectionUtils() {
        throw new UnsupportedOperationException("Utility class. Do not instantiate!");
    }

    static {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/product_database?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("12345");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setPoolName("ProductDatabasePool");
        HIKARI_DATA_SOURCE = new HikariDataSource(hikariConfig);
    }

    /**
     * Returns a connection from the pool.
     *
     * @return Connection available
     * @throws SQLException if an error occurs while obtaining the connection.
     */
    public static Connection getConnection() throws SQLException {
        return HIKARI_DATA_SOURCE.getConnection();
    }
}
