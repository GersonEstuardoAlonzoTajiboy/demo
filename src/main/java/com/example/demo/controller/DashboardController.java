package com.example.demo.controller;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Section;

import com.dlsc.formsfx.view.renderer.FormRenderer;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // Container for TilesFX widgets
    @FXML
    private HBox tileContainer;

    // Container for the form (FormsFX)
    @FXML
    private VBox formContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // --------- TilesFX: Create a gauge tile ----------
        Tile gaugeTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .prefSize(200, 200)
                .title("Temperature")
                .unit("°C")
                .value(25)
                .build();
        tileContainer.getChildren().add(gaugeTile);

        // --------- FormsFX: Create a simple form ----------
        // We create a form section with a search field.
        Form form = Form.of(
                Section.of(
                        Field.ofStringType("search")
                                .label("Search:")
                ).title("Filter Data")
        );

        // Use FormRenderer to render the form
        FormRenderer formRenderer = new FormRenderer(form);
        formContainer.getChildren().add(formRenderer);

        // --------- ControlsFX: Show a notification ----------
        Notifications.create()
                .title("Dashboard Loaded")
                .text("Welcome to the Dashboard!")
                .showInformation();
    }
}
