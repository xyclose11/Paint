package com.paint.controller;

import com.paint.handler.WorkspaceHandler;
import com.paint.model.PaintStateModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * This class controls the UI state of the ToolMenu
 * */
public class ToolMenuController {
    private static final Logger LOGGER = LogManager.getLogger();

    @FXML
    public ToggleButton toggleEraser;

    @FXML
    public Label toolMenuColorLbl;

    @FXML
    public CheckBox dashCheckbox;

    private WorkspaceHandler workspaceHandler;
    @FXML
    public ToggleButton selection;

    private PaintStateModel paintStateModel;

    private LineWidthController lineWidthController;

    private HBox lwView;

    private FXMLLoader fontToolBarLoader;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private ToggleButton currentBrushSelectedDisplay;

    @FXML
    private ToggleGroup ToolSelect; // Get toggle group for tools

    @FXML
    private ComboBox<String> toolMenuShapeLineWidthCB;

    public FXMLLoader getFontToolBarLoader() {
        return fontToolBarLoader;
    }

    public void setFontToolBarLoader(FXMLLoader fontToolBarLoader) {
        this.fontToolBarLoader = fontToolBarLoader;
    }

    // Set models
    public void setPaintStateModel(PaintStateModel paintStateModel) {
        this.paintStateModel = paintStateModel;
    }

    public WorkspaceHandler getCurrentWorkspaceModel() {
        return workspaceHandler;
    }

    public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
        this.workspaceHandler = workspaceHandler;
    }

    public void setLineWidthController(LineWidthController lineWidthController) {
        this.lineWidthController = lineWidthController;
    }

    public LineWidthController getLineWidthController() {
        return this.lineWidthController;
    }

    public void setLwView(HBox hBox) {
        this.lwView = hBox;
    }

    public HBox getLwView() {
        return lwView;
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
        // Check if LW Slider has been initialized previously
        if (this.lineWidthController == null || this.lwView == null) {
            // Set left 'Choose Line Width' slider
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/LineWidthSideView.fxml"));
            HBox temp = fxmlLoader.load();
            setLwView(temp);
            setLineWidthController(fxmlLoader.getController());
            LOGGER.info("Showing Line Width Slider");
        }

        Stage stage = (Stage) this.workspaceHandler.getCurrentScene().getWindow();

        // Set LWSlider to left side of the main BorderPane
        BorderPane bp = (BorderPane) stage.getScene().getRoot();
        bp.setLeft(lwView);

        // Add PaintStateModel to controller to alter Line Width
        this.lineWidthController.setPaintStateModel(this.paintStateModel);
    }

    private void hideLWSlider() {
        Scene scene = workspaceHandler.getCurrentScene(); // Get primary stage
        BorderPane bp = (BorderPane) scene.getRoot();
        bp.setLeft(null);
    }

    public void setColorPicker(Color color) {
        this.colorPicker.setValue(color);
    }

    @FXML
    private void onActionSetColorPicker(ActionEvent actionEvent) {
        this.paintStateModel.setCurrentPaintColor(colorPicker.getValue());
        this.colorPicker.valueProperty().bindBidirectional(this.paintStateModel.getCurrentPaintColorProperty());
        Color color = this.paintStateModel.getCurrentPaintColor();
        DecimalFormat decimalFormat = new DecimalFormat("000");
        String redV = decimalFormat.format(color.getRed() * 255); // Multiply by 255 since the range of getRed/Blue/Green -> 0.0 - 1.0
        String blueV = decimalFormat.format(color.getBlue() * 255);
        String greenV = decimalFormat.format(color.getGreen() * 255);

        this.toolMenuColorLbl.setText("HEX: " + color + " RGB: " + redV + "," + greenV + "," + blueV);
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
                        return;
	                } catch (IOException e) {
		                throw new RuntimeException(e);
	                }
                }

                // Check for Text Tool selection -> show tool menu for Font
                if (val.contains("TextTool")) {
//                    // Load the FXML for the Font controls
//                    try {
////                        AnchorPane dialogPane = fontToolBarLoader.load();
////                        Parent root = workspaceHandler.getCurrentScene().getRoot();
//
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }

                }

                // Hide Line Width Slider
                hideLWSlider();


            }
        }));
    }

    @FXML
    private void onMouseClickedDash(MouseEvent mouseEvent) {
        this.paintStateModel.setDashed(dashCheckbox.isSelected());
    }

    public void updateSelectionToolState(MouseEvent mouseEvent) {
        String sourceId = ((Control)mouseEvent.getSource()).getId();

        if (sourceId == null) {
            showToolIsNullAlert();
        }
        // Update paintStateModel current tool when a new tool is selected
        this.paintStateModel.setCurrentTool(sourceId);
        this.paintStateModel.setCurrentToolType("selection");
    }


}
