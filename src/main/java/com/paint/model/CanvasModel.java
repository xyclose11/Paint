package com.paint.model;

import com.paint.resource.ResizeableCanvas;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

// Model to hold canvas information
public class CanvasModel {
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();
    private Group canvasGroup;
    private boolean isFileBlank = true; // On open file is blank
    private boolean changesMade;
    private HBox canvasView;

    public HBox getCanvasView() {
        return canvasView;
    }

    public void setCanvasView(HBox canvasView) {
        this.canvasView = canvasView;
    }

    public void clearCanvas() {
        // Show alert
        Alert clearCanvasAlert = new Alert(Alert.AlertType.WARNING, """
                            WARNING: You are about to clear the canvas. Any unsaved changes will be lost.
                            Do you still want to continue?
                            """);
        clearCanvasAlert.setTitle("WARNING: You're changes will not be saved.");
        clearCanvasAlert.setHeaderText("");
        clearCanvasAlert.showAndWait();

        StackPane stackPane = (StackPane) this.canvasGroup.getChildren().get(0);
        ResizeableCanvas resizeableCanvas = (ResizeableCanvas) stackPane.getChildren().get(0);
        resizeableCanvas.getGraphicsContext2D().clearRect(0, 0, resizeableCanvas.getWidth(), resizeableCanvas.getHeight());

    }

    private final DoubleProperty zoomScale = new SimpleDoubleProperty(1.0); // 1.0 = 100%
    private double maxScale = 8.0;
    private double minScale = 0.10;

    public double getMaxScale() {
        return maxScale;
    }

    public double getMinScale() {
        return minScale;
    }

    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    public DoubleProperty zoomScaleProperty() {
        return zoomScale;
    }

    public double getZoomScale() {
        return zoomScale.getValue();
    }

    public void setZoomScale(double zoomScale) {
        if (zoomScale > maxScale) {
            this.zoomScale.setValue(maxScale);
            return;
        } else if (zoomScale < minScale) {
            this.zoomScale.setValue(minScale);
            return;
        }
        this.zoomScale.setValue(zoomScale);
    }

    public boolean isChangesMade() {
        return changesMade;
    }

    public void setChangesMade(boolean changesMade) {
        this.changesMade = changesMade;
    }

    public boolean isFileBlank() {
        return isFileBlank;
    }

    public void setFileBlank(boolean fileBlank) {
        isFileBlank = fileBlank;
    }

    public Group getCanvasGroup() {
        return canvasGroup;
    }

    public void setCanvasGroup(Group canvasGroup) {
        this.canvasGroup = canvasGroup;
    }

    public DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    public DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    public double getCanvasWidth() {
        return canvasWidth.get();
    }

    public void setCanvasWidth(double width) {
        canvasWidth.set(width);
    }

    public double getCanvasHeight() {
        return canvasHeight.get();
    }

    public void setCanvasHeight(double height) {
        canvasHeight.set(height);
    }

}
