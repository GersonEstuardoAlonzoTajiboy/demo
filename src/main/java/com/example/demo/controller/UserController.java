package com.example.demo.controller;

import com.example.demo.models.RoleModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @FXML
    private TextField fullNameTextField;

    @FXML
    private TextField emailTextField;

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
        String fullName = fullNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        RoleModel selectedRole = roleComboBox.getSelectionModel().getSelectedItem();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            LOGGER.warning("All fields are required.");
        }

        UserModel userModel = new UserModel(0, fullName, email, password, selectedRole);

        UserRepository.insertUser(userModel);

        LOGGER.info("User saved.");
    }
}
