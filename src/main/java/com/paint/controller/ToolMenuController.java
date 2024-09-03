package com.paint.controller;

import com.paint.model.PaintStateModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.input.MouseEvent;

public class ToolMenuController {
    private PaintStateModel paintStateModel;


    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
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
    public void updateMenuItemToolState(ActionEvent actionEvent) {
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
}
