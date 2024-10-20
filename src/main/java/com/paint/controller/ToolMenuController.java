package com.paint.controller;

import com.paint.handler.WorkspaceHandler;
import com.paint.model.InfoCanvasModel;
import com.paint.model.PaintStateModel;
import com.paint.resource.ResizeableCanvas;
import com.paint.resource.TransformableNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

    @FXML
	public MenuButton mirrorMenuToolMenu;

    @FXML
    public MenuItem rotateMenuItemRight;

    @FXML
    public MenuButton rotateMenu;

    @FXML
    public MenuItem rotateMenuItemLeft;

    @FXML
    public MenuItem rotateMenuItem180;

    @FXML
    public MenuItem horizontalFlip;

    @FXML
    public MenuItem verticalFlip;

    private WorkspaceHandler workspaceHandler;
    @FXML
    public ToggleButton selection;

    private PaintStateModel paintStateModel;
    private InfoCanvasModel infoCanvasModel;

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
        // flip events
        this.horizontalFlip.setOnAction(this::handleHorizontalFlip);
        this.verticalFlip.setOnAction(this::handleVerticalFlip);


        // rotate events
        this.rotateMenuItemLeft.setOnAction(this::handleRotateLeftEvent);
        this.rotateMenuItemRight.setOnAction(this::handleRotateRightEvent);
        this.rotateMenuItem180.setOnAction(this::handleRotate180Event);

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

                // Hide Line Width Slider
                hideLWSlider();
            }
        }));
    }

    private void handleRotateRightEvent(ActionEvent event) {
        if (this.paintStateModel.getCurrentNode() == null) {
            rotateCanvas("right");
            return;
        }

        TransformableNode currentNode = this.paintStateModel.getCurrentNode();

        currentNode.rotate90Right();

    }

    private void handleRotateLeftEvent(ActionEvent event) {
        if (this.paintStateModel.getCurrentNode() == null) {
            rotateCanvas("left");
            return;
        }

        TransformableNode currentNode = this.paintStateModel.getCurrentNode();

        currentNode.rotate90Left();
    }

    private void handleRotate180Event(ActionEvent event) {
        if (this.paintStateModel.getCurrentNode() == null) {
            rotateCanvas("180");
            return;
        }

        TransformableNode currentNode = this.paintStateModel.getCurrentNode();

        currentNode.rotate180();
    }

    // rotate canvas if no object is selected
    private void rotateCanvas(String direction) {
        StackPane stackPane = (StackPane) this.workspaceHandler.getCurrentWorkspace().getCanvasModel().getCanvasGroup().getChildren().get(0);
        ResizeableCanvas resizeableCanvas = (ResizeableCanvas) stackPane.getChildren().get(0);
        
        if (direction.contains("right")) {
            resizeableCanvas.rotate90Right();
            infoCanvasModel.setResolutionLblText(resizeableCanvas.getWidth(),resizeableCanvas.getHeight());
        } else if (direction.contains("left")) {
            resizeableCanvas.rotate90Left();
            infoCanvasModel.setResolutionLblText(resizeableCanvas.getWidth(),resizeableCanvas.getHeight());
        } else if (direction.contains("180")){
            resizeableCanvas.rotate180();
        }
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

    @FXML
    private void handleHorizontalFlip(ActionEvent event) {
        ResizeableCanvas resizeableCanvas = getResizeableCanvas();
        resizeableCanvas.horizontalFlip();
    }

    @FXML
    private void handleVerticalFlip(ActionEvent event) {
        ResizeableCanvas resizeableCanvas = getResizeableCanvas();
        resizeableCanvas.verticalFlip();
    }

    private ResizeableCanvas getResizeableCanvas() {
        StackPane stackPane = (StackPane) this.workspaceHandler.getCurrentWorkspace().getCanvasModel().getCanvasGroup().getChildren().get(0);
        return (ResizeableCanvas) stackPane.getChildren().get(0);
    }

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

    public InfoCanvasModel getInfoCanvasModel() {
        return infoCanvasModel;
    }

    public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
        this.infoCanvasModel = infoCanvasModel;
    }
}
