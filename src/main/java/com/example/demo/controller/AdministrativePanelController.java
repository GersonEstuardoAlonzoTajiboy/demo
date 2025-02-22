package com.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private StackPane contentArea;

    @FXML
    private void initialize() {
        // Setting events for navigation buttons
        dashboardButton.setOnAction(event -> loadView("/fxml/Dashboard.fxml"));

        // Load the "Dashboard" view by default
        loadView("/fxml/Dashboard.fxml");

        // --------- ControlsFX: Show a notification ----------
        Notifications.create()
                .title("Administrative Panel Loaded")
                .text("Welcome to the Administrative Panel!")
                .showInformation();
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
            LOGGER.log(Level.SEVERE, "Error loading view " + fxmlPath, ioException);
        }
    }
}
