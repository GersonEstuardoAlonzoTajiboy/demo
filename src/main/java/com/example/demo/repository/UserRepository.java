package com.example.demo.repository;

import com.example.demo.models.UserModel;
import com.example.demo.utils.DBConnectionUtils;

import java.sql.*;
import java.util.logging.Logger;

public class UserRepository {

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

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
}
