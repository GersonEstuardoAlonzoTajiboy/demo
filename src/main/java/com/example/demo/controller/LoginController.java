package com.example.demo.controller;

import com.example.demo.models.LoginModel;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.SecurityUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.synedra.validatorfx.Validator;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the login form.
 * Validates credentials against the database via UserRepository and navigates to the dashboard upon success.
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
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private Stage primaryStage;
    private Timeline debounceTimeline;

    private final Validator validator = new Validator();
    private final LoginModel loginModel = new LoginModel();


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

        // Disable the login button if the form is invalid (using the valid property of the model)
        loginButton.disableProperty().bind(loginModel.validProperty().not());

        // Configure debounce on username and password validation
        usernameField.addEventHandler(KeyEvent.KEY_RELEASED, event -> debounceValidation());
        passwordField.addEventHandler(KeyEvent.KEY_RELEASED, event -> debounceValidation());

        // Real-time validation for the username field
        validator.createCheck()
                .dependsOn("username", loginModel.usernameProperty())
                .withMethod(context -> {
                    String username = loginModel.getUsername();
                    if (username == null || username.trim().isEmpty()) {
                        context.error("Username is required");
                    } else if (!username.matches("[a-zA-Z0-9]+")) {
                        context.error("Username must be alphanumeric");
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
                    } else if (password.contains(" ")) {
                        context.error("Password must not contain spaces");
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

        // Get credentials
        final String username = loginModel.getUsername();
        final String password = loginModel.getPassword();

        // Additional security validation
        if (!username.matches("[a-zA-Z0-9]+")) {
            errorLabel.setText("Invalid username format.");
            errorLabel.setVisible(true);
            return;
        }

        // Disable button while processing
        loginButton.setDisable(true);

        // Perform the query asynchronously using the static repository
        CompletableFuture.supplyAsync(() -> {
                    try {
                        // We use the static method to get the user by username
                        return UserRepository.findUserByCredentials(username, password);
                    } catch (Exception exception) {
                        LOGGER.log(Level.SEVERE, "Error during authentication", exception);
                        return null;
                    }
                })
                .thenAccept(userModel -> Platform.runLater(() -> {
                    loginButton.setDisable(false);
                    if (userModel != null) {
                        // Verify the password using the hash
                        if (SecurityUtil.verifyPassword(password, userModel.getPassword())) {
                            errorLabel.setVisible(false);
                            navigateToAdministrativePanel();
                        } else {
                            errorLabel.setText("Invalid credentials.");
                            errorLabel.setVisible(true);
                        }
                    } else {
                        errorLabel.setText("Invalid credentials.");
                        errorLabel.setVisible(true);
                    }
                }));
    }

    /**
     * Navigate to the administrative panel by loading the corresponding FXML.
     */
    private void navigateToAdministrativePanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/administrative-panel.fxml"));
            Parent dashboardRoot = loader.load();
            Scene dashboardScene = new Scene(dashboardRoot);
            dashboardScene.getStylesheets().add(
                    Objects.requireNonNull(
                                    getClass().getResource("/css/administrative-panel.css")
                            )
                            .toExternalForm()
            );
            primaryStage.setScene(dashboardScene);
        } catch (IOException ioException) {
            LOGGER.log(Level.SEVERE, "Error navigating to administrative panel", ioException);
        }
    }

    private void debounceValidation() {
        if (debounceTimeline != null) {
            debounceTimeline.stop();
        }
        debounceTimeline = new Timeline(new KeyFrame(Duration.millis(300), event -> validator.validate()));
        debounceTimeline.play();
    }
}
