package com.example.demo.controller;

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
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error loading login screen during logout", ex);
        }
    }


    /**
     * Loads a view into the content area.
     *
     * @param fxmlPath path of the FXML file to load.
     */
    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            contentArea.getChildren().setAll(view);
        } catch (IOException ioException) {
            LOGGER.log(Level.SEVERE, String.format("Error loading view %s", fxmlPath), ioException);
        }
    }
}
