package com.example.demo.repository;

import com.example.demo.models.RoleModel;
import com.example.demo.models.UserModel;
import com.example.demo.utils.DBConnectionUtils;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Repository for the User entity.
 */
public class UserRepository {

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

    /**
     * Inserts a new user into the database.
     *
     * @param userModel User to insert.
     */
    public static void insertUser(UserModel userModel) {
        String sql = """
                INSERT INTO users (username, password, role_id)
                VALUES (?, ?, ?)
                """;
        try (
                Connection connection = DBConnectionUtils.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            preparedStatement.setString(1, userModel.getUsername());
            preparedStatement.setString(2, userModel.getPassword());
            if (userModel.getRole() != null) {
                preparedStatement.setInt(3, userModel.getRole().getId());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        userModel.setId(resultSet.getInt(1));
                    }
                }
            }
        } catch (SQLException sqlException) {
            LOGGER.info(sqlException.getMessage());
        }
    }

    /**
     * Query the database to get a user based on the credentials.
     *
     * @param username User logged in.
     * @param password Password entered.
     * @return A UserModel object if a match is found, or null otherwise.
     */
    public static UserModel findUserByCredentials(String username, String password) {
        String sql = """
                SELECT u.id, u.username, u.password, r.id AS role_id, r.name AS role_name
                FROM users AS u
                LEFT JOIN roles AS r ON r.id = u.role_id
                WHERE u.username = ? AND u.password = ?
                """;
        try (
                Connection connection = DBConnectionUtils.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If the role is present the RoleModel object is created, otherwise it is left as null
                    int roleId = resultSet.getInt("role_id");
                    String roleName = resultSet.getString("role_name");
                    RoleModel roleModel = roleName != null ? new RoleModel(roleId, roleName) : null;

                    // Returns the user found
                    return new UserModel(
                            resultSet.getInt("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password"),
                            roleModel
                    );
                }
            }
        } catch (SQLException sqlException) {
            LOGGER.info(sqlException.getMessage());
        }
        return null;
    }
}
