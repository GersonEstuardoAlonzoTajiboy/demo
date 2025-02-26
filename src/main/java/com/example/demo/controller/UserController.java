package com.example.demo.controller;

import com.example.demo.models.RoleModel;
import com.example.demo.models.UserModel;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UserController {

    @FXML
    private TableView<UserModel> usersTable;

    @FXML
    private TableColumn<UserModel, Integer> idColumn;

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

    private final ObservableList<UserModel> userModelObservableList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Load the roles from the database
        List<RoleModel> roleModelList = RoleRepository.getAllRoles();
        ObservableList<RoleModel> roleModelObservableList = FXCollections.observableList(roleModelList);
        roleComboBox.setItems(roleModelObservableList);

        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleNameColumn.setCellValueFactory(cellDataFeatures -> {
            RoleModel roleModel = cellDataFeatures.getValue().getRole();
            return roleModel != null ? roleModel.nameProperty() : null;
        });

        // Load the list of users
        loadUsers();

        // Listener to load data into the form when a user selects it
        usersTable.getSelectionModel().selectedItemProperty().addListener((
                observable,
                oldValue,
                newValue) -> {
            if (newValue != null) {
                usernameTextField.setText(newValue.getUsername());
                passwordTextField.setText(newValue.getPassword());
                roleComboBox.getSelectionModel().select(newValue.getRole());
            }
        });
    }

    @FXML
    private void handleCreate() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        RoleModel selectedRole = roleComboBox.getSelectionModel().getSelectedItem();
        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "All fields are required.");
        }

        UserModel userModel = new UserModel(0, username, password, selectedRole);
        UserRepository.insertUser(userModel);
        showAlert(Alert.AlertType.INFORMATION, "User creation", "User created successfully.");
        loadUsers();
        clearFields();
    }

    @FXML
    private void handleUpdate() {
        UserModel selectedUserModel = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUserModel == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Please select a user to update.");
            return;
        }

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        RoleModel selectedRole = roleComboBox.getSelectionModel().getSelectedItem();
        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert(Alert.AlertType.WARNING, "Validation", "All fields are required.");
            return;
        }

        selectedUserModel.setUsername(username);
        selectedUserModel.setPassword(password);
        selectedUserModel.setRole(selectedRole);
        UserRepository.updateUser(selectedUserModel);
        showAlert(Alert.AlertType.INFORMATION, "User updated", "User updated successfully.");
        loadUsers();
        clearFields();
    }

    @FXML
    private void handleDelete() {
        UserModel selectedUserModel = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUserModel == null) {
            showAlert(Alert.AlertType.WARNING, "No selection", "Please select a user to delete");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to delete the selected user?");
        if (confirmationAlert.showAndWait().filter(response -> response == ButtonType.OK).isPresent()) {
            UserRepository.deleteUser(selectedUserModel.getId());
            showAlert(Alert.AlertType.INFORMATION, "User Deletion", "User deleted successfully.");
            loadUsers();
            clearFields();
        }
    }

    private void loadUsers() {
        List<UserModel> userModelList = UserRepository.getAllUsers();
        userModelObservableList.setAll(userModelList);
        usersTable.setItems(userModelObservableList);
    }

    private void clearFields() {
        usernameTextField.clear();
        passwordTextField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
