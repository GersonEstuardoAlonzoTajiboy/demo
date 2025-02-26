package com.example.demo.controller;

import com.example.demo.models.RoleModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.logging.Logger;

public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @FXML
    private TableView<UserModel> userTable;

    @FXML
    private TableColumn<UserModel, Integer> userIdColumn;

    @FXML
    private TableColumn<UserModel, String> usernameColumn;

    @FXML
    private TableColumn<UserModel, String> roleNameColumn;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ComboBox<RoleModel> roleComboBox;

    private ObservableList<RoleModel> rolesModelObservableList = FXCollections.observableArrayList(
            new RoleModel(1, "ADMINISTRATOR"),
            new RoleModel(2, "USER")
    );

    @FXML
    private void initialize() {
        roleComboBox.setItems(rolesModelObservableList);
    }

    @FXML
    private void handleSave() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        RoleModel selectedRole = roleComboBox.getSelectionModel().getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            LOGGER.warning("All fields are required.");
        }

        UserModel userModel = new UserModel(0, username, password, selectedRole);

        UserRepository.insertUser(userModel);

        LOGGER.info("User saved.");
    }
}
