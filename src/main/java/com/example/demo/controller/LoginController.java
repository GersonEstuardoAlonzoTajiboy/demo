package com.example.demo.controller;

import com.example.demo.models.LoginModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.synedra.validatorfx.Validator;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Controller for the login form.
 * Integrates validations with ValidatorFX, uses a dummy user (admin/password)
 * and redirects to the dashboard upon successful login.
 */
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @FXML
    private Label titleLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public Button loginButton;

    @FXML
    private Label errorLabel;

    private Stage primaryStage;
    private final Validator validator = new Validator();
    private final LoginModel loginModel = new LoginModel();

    // Dummy credentials for testing
    private static final String DUMMY_USERNAME = "dummy_username@gmail.com";
    private static final String DUMMY_PASSWORD = "dummy_password_abc";

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void initialize() {
        // Lock icon using Ikonli
        FontIcon lockIcon = new FontIcon("fas-lock");
        lockIcon.setIconSize(48);
        lockIcon.setIconColor(javafx.scene.paint.Color.web("#6200EA"));

        // Title tag with icon
        titleLabel.setGraphic(lockIcon);
        titleLabel.setContentDisplay(ContentDisplay.TOP);

        // Link the text fields to the login model
        usernameField.textProperty().bindBidirectional(loginModel.usernameProperty());
        passwordField.textProperty().bindBidirectional(loginModel.passwordProperty());

        // Real-time validation for the username field
        validator.createCheck()
                .dependsOn("username", loginModel.usernameProperty())
                .withMethod(context -> {
                    String username = loginModel.getUsername();
                    if (username == null || username.trim().isEmpty()) {
                        context.error("Username is required");
                    }
                })
                .decorates(usernameField)
                .recheck();

        // Real-time validation for the password field
        validator.createCheck()
                .dependsOn("password", loginModel.passwordProperty())
                .withMethod(context -> {
                    String password = loginModel.getPassword();
                    if (password == null || password.trim().isEmpty()) {
                        context.error("Password is required");
                    }
                })
                .decorates(passwordField)
                .recheck();
    }

    @FXML
    private void handleLogin() {
        // Run validations
        validator.validate();
        if (!validator.getValidationResult().getMessages().isEmpty()) {
            errorLabel.setText("Please fix the errors.");
            errorLabel.setVisible(true);
            return;
        }

        // Check credentials using the model
        if (loginModel.getUsername().equals(DUMMY_USERNAME) &&
                passwordField.getText().equals(DUMMY_PASSWORD)) {
            errorLabel.setVisible(false);
            navigateToDashboard();
        } else {
            errorLabel.setText("Invalid credentials!");
        }
    }

    /**
     * Navigate to the dashboard by loading the corresponding FXML.
     */
    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);
            dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/dashboard.css")).toExternalForm());
            primaryStage.setScene(dashboardScene);
        } catch (IOException ioException) {
            LOGGER.info(ioException.getMessage());
        }
    }
}
