package com.example.demo.controller;

import com.example.demo.models.SessionManager;
import com.example.demo.models.UserModel;
import com.example.demo.utils.ViewCacheUtil;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdministrativePanelController {

    private static final Logger LOGGER = Logger.getLogger(AdministrativePanelController.class.getName());

    @FXML
    private VBox navContainer;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button usersButton;

    @FXML
    private Button productsButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private StackPane contentArea;

    @FXML
    private void initialize() {
        // Check the authenticated user's role and adjust the UI
        UserModel currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && !"ADMINISTRATOR".equalsIgnoreCase(currentUser.getRole().getName())) {
            usersButton.setDisable(true);
            usersButton.setVisible(false);
        }

        // Setting events for navigation buttons
        dashboardButton.setOnAction(event -> loadView("/fxml/dashboard.fxml"));
        usersButton.setOnAction(event -> loadView("/fxml/user.fxml"));

        // Load the "Dashboard" view by default
        loadView("/fxml/dashboard.fxml");

        // --------- ControlsFX: Show a notification ----------
        Notifications.create()
                .title("Administrative Panel Loaded")
                .text("Welcome to the Administrative Panel!")
                .showInformation();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent loginRoot = loader.load();

            // Get the Login controller
            LoginController loginController = loader.getController();

            // Get the current Stage from a node (e.g. logoutButton or any other node)
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Sets the Stage reference in the login controller
            loginController.setPrimaryStage(stage);

            Scene loginScene = new Scene(loginRoot);
            loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/login.css")).toExternalForm());
            stage.setScene(loginScene);
            stage.setTitle("Login");

            // Clear the current session
            SessionManager.getInstance().setCurrentUser(null);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading login screen during logout", ex);
        }
    }


    /**
     * Loads a view into the content area using caching and asynchronous loading.
     *
     * @param fxmlPath path of the FXML file to load.
     */
    private void loadView(String fxmlPath) {
        // First we try to get the view from the cache
        Node cacheView = ViewCacheUtil.getView(fxmlPath);
        if (cacheView != null) {
            contentArea.getChildren().setAll(cacheView);
            return;
        }

        // Configure and execute asynchronous loading
        Task<Node> loadTask = createViewLoadingTask(fxmlPath);
        startBackgroundThread(loadTask);
    }

    /**
     * Creates and configures a Task for asynchronous view loading.
     *
     * @param fxmlPath FXML file path to load.
     * @return Configured Task instance with success/error handlers.
     */
    private Task<Node> createViewLoadingTask(String fxmlPath) {
        Task<Node> loadTask = new Task<>() {
            @Override
            protected Node call() throws Exception {
                return ViewCacheUtil.loadView(fxmlPath);
            }
        };

        // Success handler: Update UI with loaded view
        loadTask.setOnSucceeded(event -> {
            Node view = loadTask.getValue();
            contentArea.getChildren().setAll(view);
        });

        // Error handler: Log exception with proper message formatting
        loadTask.setOnFailed(event -> {
            Throwable throwable = loadTask.getException();
            LOGGER.log(Level.SEVERE, throwable, () -> "Error loading view" + fxmlPath);
        });
        return loadTask;
    }

    /**
     * Starts thread for task execution to prevent UI blocking.
     *
     * @param task Task to execute in background.
     */
    private void startBackgroundThread(Task<Node> task) {
        // Use a thread to allow graceful shutdown of the JVM
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
