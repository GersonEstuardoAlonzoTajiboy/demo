package com.example.demo.repository;

import com.example.demo.models.RoleModel;
import com.example.demo.models.UserModel;
import com.example.demo.utils.DBConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Repository for the User entity.
 */
public class UserRepository {

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

    private UserRepository() {
        throw new UnsupportedOperationException("Repository class. Do not instantiate!");
    }

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
                Connection connection = DBConnectionUtil.getConnection();
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
     * Updates an existing user in the database.
     *
     * @param userModel The user to update.
     */
    public static void updateUser(UserModel userModel) {
        String sql = """
                UPDATE users
                SET username = ?, password = ?, role_id = ?
                WHERE id = ?
                """;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, userModel.getUsername());
            preparedStatement.setString(2, userModel.getPassword());
            if (userModel.getRole() != null) {
                preparedStatement.setInt(3, userModel.getRole().getId());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }
            preparedStatement.setInt(4, userModel.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LOGGER.info(sqlException.getMessage());
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId The id of the user to delete.
     */
    public static void deleteUser(int userId) {
        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            LOGGER.info(sqlException.getMessage());
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of UserModel objects.
     */
    public static List<UserModel> getAllUsers() {
        List<UserModel> usersModelList = new ArrayList<>();
        String sql = """
                SELECT u.id, u.username, u.password, r.id AS role_id, r.name AS role_name
                FROM users u
                LEFT JOIN roles r ON u.role_id = r.id;
                """;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                int roleId = resultSet.getInt("role_id");
                String roleName = resultSet.getString("role_name");
                RoleModel roleModel = roleName != null ? new RoleModel(roleId, roleName) : null;
                usersModelList.add(new UserModel(id, username, password, roleModel));
            }
        } catch (SQLException sqlException) {
            LOGGER.info(sqlException.getMessage());
        }
        return usersModelList;
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
                WHERE u.username = ?
                """;
        try (
                Connection connection = DBConnectionUtil.getConnection();
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
