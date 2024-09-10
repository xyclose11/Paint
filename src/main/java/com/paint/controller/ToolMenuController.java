package com.paint.controller;

import com.paint.model.PaintStateModel;
import com.paint.model.SceneStateModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private ColorPicker colorPicker;

    @FXML
    private ToggleButton currentBrushSelectedDisplay;

    @FXML
    private ToggleGroup ToolSelect; // Get toggle group for tools

    @FXML
    private ComboBox<String> toolMenuShapeLineWidthCB;

    // Set models
    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
    }
    public void setSceneStateModel(SceneStateModel sceneStateModel) {
        this.sceneStateModel = sceneStateModel;
    }

    @FXML
    public void updateShapeToolState(MouseEvent mouseEvent) {
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

        // Update currentBrushSelectedDisplay view to be selected
        currentBrushSelectedDisplay.setSelected(true);
    }

    // General tool event handler
    @FXML
    public void updateGeneralToolState(MouseEvent mouseEvent) throws IOException {
        String sourceId = ((Control)mouseEvent.getSource()).getId();

        if (sourceId == null) {
            showToolIsNullAlert();
        }
        // Update paintStateModel current tool when a new tool is selected
        this.paintStateModel.setCurrentTool(sourceId);
        this.paintStateModel.setCurrentToolType("general");
    }



    private void showToolIsNullAlert() {
        // Show alert if selected tool does not respond properly
        Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, "DESIRED TOOL SELECT IS NULL. PLEASE USE A DIFFERENT TOOL.");
        noToolSelectedAlert.setTitle("ERROR: TOOL SELECTION IS NULL");
        noToolSelectedAlert.setHeaderText("");
        noToolSelectedAlert.showAndWait();
    }

    public void showLWSlider() throws IOException {
        // Set left 'Choose Line Width' slider
        FXMLLoader lWLoader = new FXMLLoader(getClass().getResource("/view/LineWidthSideView.fxml"));
        Stage stage = (Stage) this.sceneStateModel.getCurrentScene().getWindow();

        // Set LWSlider to left side of the main BorderPane
        BorderPane bp = (BorderPane) stage.getScene().getRoot();
        bp.setLeft(lWLoader.load());

        // Add PaintStateModel to controller to alter Line Width
        LineWidthController lineWidthController = lWLoader.getController();
        lineWidthController.setPaintStateModel(this.paintStateModel);
    }

    private void hideLWSlider(Toggle toggle) {
        Scene scene = sceneStateModel.getCurrentScene(); // Get primary stage
        BorderPane bp = (BorderPane) scene.getRoot();
        bp.setLeft(null);
    }

    @FXML
    private void onActionSetColorPicker(ActionEvent actionEvent) {
        this.paintStateModel.setCurrentPaintColor(colorPicker.getValue());
    }

    @FXML
    private void onShapeLineWidthChange(ActionEvent actionEvent) {
        double cbVal = 1.0; // Default LW for shapes
        try {
            String tempV = this.toolMenuShapeLineWidthCB.getValue();

            // TODO implement more thorough validation since you can input any other string and it will pass this test
            if (tempV.lastIndexOf("px") != -1) { // Meaning that there is a 'px' string pair somewhere in the String
                tempV = tempV.substring(0, tempV.length() - 2);
            }
            cbVal = Double.parseDouble(tempV); // Convert user input 'String -> double'

        } catch (Exception e) {
            Alert noToolSelectedAlert = new Alert(Alert.AlertType.ERROR, """
                    ERROR: UNABLE TO CONVERT SHAPE LINE WIDTH STRING -> DOUBLE. ERROR: 
                    """ + e.getMessage());
            noToolSelectedAlert.setTitle("No Tool Selected: LW CHANGE");
            noToolSelectedAlert.setHeaderText("");
            noToolSelectedAlert.showAndWait();
            e.printStackTrace();
        }

        // Update paintStateModel fields
        this.paintStateModel.setCurrentShapeLineStrokeWidth(cbVal);
    }

    @FXML
    public void initialize() {
        // Add event listener to check if a tool that has the line width property is selected
        ToolSelect.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String val = newValue.toString();
                if (val.contains("regular") || val.contains("eraser")) { // Looks for 'regular' brush type
                    // Show Line Width Slider
	                try {
		                showLWSlider();
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
