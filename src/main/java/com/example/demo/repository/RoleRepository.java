package com.example.demo.repository;

import com.example.demo.models.RoleModel;
import com.example.demo.utils.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RoleRepository {

    private static final Logger LOGGER = Logger.getLogger(RoleRepository.class.getName());

    private RoleRepository() {
        throw new UnsupportedOperationException("Repository class. Do not instantiate!");
    }

    /**
     * Retrieves all roles from the database.
     *
     * @return A list of RoleModel objects.
     */
    public static List<RoleModel> getAllRoles() {
        List<RoleModel> rolesModelList = new ArrayList<>();
        String sql = """
                SELECT id, name
                FROM roles
                """;
        try (Connection connection = DBConnectionUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                rolesModelList.add(new RoleModel(
                                resultSet.getInt("id"),
                                resultSet.getString("name")
                        )
                );
            }
        } catch (SQLException sqlException) {
            LOGGER.info(sqlException.getMessage());
        }
        return rolesModelList;
    }
}
