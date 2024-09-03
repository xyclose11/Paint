package com.paint.controller;

import com.paint.model.PaintStateModel;
import com.paint.model.SceneStateModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ToolMenuController {
    private PaintStateModel paintStateModel;

    private SceneStateModel sceneStateModel;

    @FXML
    private ToggleGroup ToolSelect; // Get toggle group for tools

    // Set models
    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
    }
    public void setSceneStateModel(SceneStateModel sceneStateModel) {
        this.sceneStateModel = sceneStateModel;
    }

    @FXML
    public void updateToolState(MouseEvent mouseEvent) {
        String sourceId = ((Control)mouseEvent.getSource()).getId();

        if (sourceId == null) {
            showToolIsNullAlert();
        }
        // Update paintStateModel current tool when a new tool is selected
        this.paintStateModel.setCurrentTool(sourceId);
        this.paintStateModel.setCurrentToolType("shape");
    }

    // Handle event if RadioMenuItem is selected (Used in Brush dropdown menu)
    @FXML
    public void updateMenuItemToolState(ActionEvent actionEvent) throws IOException {
        String sourceId = ((RadioMenuItem)actionEvent.getSource()).getId();

        if (sourceId == null) {
            showToolIsNullAlert();
        }
        // Update paintStateModel current tool when a new tool is selected
        this.paintStateModel.setCurrentTool(sourceId);
        this.paintStateModel.setCurrentToolType("brush");
    }

    private void showToolIsNullAlert() {
        // Show alert if selected tool does not respond properly
        Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "DESIRED TOOL SELECT IS NULL. PLEASE USE A DIFFERENT TOOL.");
        noToolSelectedAlert.setTitle("ERROR: TOOL SELECTION IS NULL");
        noToolSelectedAlert.setHeaderText("");
        noToolSelectedAlert.showAndWait();
    }

    private void showLWSlider(RadioMenuItem radioItem) throws IOException {
        // Set left 'Choose Line Width' slider
        Parent root = FXMLLoader.load(getClass().getResource("/view/LineWidthSideView.fxml"));
        Stage stage = (Stage)radioItem.getParentPopup().getOwnerWindow(); // Get primary stage

        BorderPane bp = (BorderPane) stage.getScene().getRoot();
        bp.setLeft(root);
    }

    private void hideLWSlider(Toggle toggle) {
        Scene scene = sceneStateModel.getCurrentScene(); // Get primary stage

        BorderPane bp = (BorderPane) scene.getRoot();
        bp.setLeft(null);
    }

    @FXML
    public void initialize() {
        ToolSelect.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue instanceof RadioMenuItem) {
                    // Show Line Width Slider
	                try {
		                showLWSlider((RadioMenuItem) newValue);
	                } catch (IOException e) {
		                throw new RuntimeException(e);
	                }
                } else {
                    // Hide Line Width Slider
                    hideLWSlider(newValue);
                }
            }
        }));
    }
}
