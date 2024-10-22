package com.paint.model;

import com.paint.resource.ResizeableCanvas;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * This model holds the state of canvas related values, such as Canvas Height, Canvas Width,
 * the CanvasView (FXML), and the state of changes made
 *
 * @since 1.1
 * */
public class CanvasModel {
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();
    private Group canvasGroup;
    private boolean isFileBlank = true; // On open file is blank
    private boolean changesMade;
    private HBox canvasView;

    /**
     * Gets canvas view.
     *
     * @return the canvas view
     */
    public HBox getCanvasView() {
        return canvasView;
    }

    /**
     * Sets canvas view.
     *
     * @param canvasView the canvas view
     */
    public void setCanvasView(HBox canvasView) {
        this.canvasView = canvasView;
    }

    /**
     * Clear canvas.
     */
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

    /**
     * Gets max scale.
     *
     * @return the max scale
     */
    public double getMaxScale() {
        return maxScale;
    }

    /**
     * Gets min scale.
     *
     * @return the min scale
     */
    public double getMinScale() {
        return minScale;
    }

    /**
     * Sets max scale.
     *
     * @param maxScale the max scale
     */
    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    /**
     * Sets min scale.
     *
     * @param minScale the min scale
     */
    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    /**
     * Zoom scale property double property.
     *
     * @return the double property
     */
    public DoubleProperty zoomScaleProperty() {
        return zoomScale;
    }

    /**
     * Gets zoom scale.
     *
     * @return the zoom scale
     */
    public double getZoomScale() {
        return zoomScale.getValue();
    }

    /**
     * Sets zoom scale.
     *
     * @param zoomScale the zoom scale
     */
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

    /**
     * Is changes made boolean.
     *
     * @return the boolean
     */
    public boolean isChangesMade() {
        return changesMade;
    }

    /**
     * Sets changes made.
     *
     * @param changesMade the changes made
     */
    public void setChangesMade(boolean changesMade) {
        this.changesMade = changesMade;
    }

    /**
     * Is file blank boolean.
     *
     * @return the boolean
     */
    public boolean isFileBlank() {
        return isFileBlank;
    }

    /**
     * Sets file blank.
     *
     * @param fileBlank the file blank
     */
    public void setFileBlank(boolean fileBlank) {
        isFileBlank = fileBlank;
    }

    /**
     * Gets canvas group.
     *
     * @return the canvas group
     */
    public Group getCanvasGroup() {
        return canvasGroup;
    }

    /**
     * Sets canvas group.
     *
     * @param canvasGroup the canvas group
     */
    public void setCanvasGroup(Group canvasGroup) {
        this.canvasGroup = canvasGroup;
    }

    /**
     * Canvas width property double property.
     *
     * @return the double property
     */
    public DoubleProperty canvasWidthProperty() {
        return canvasWidth;
    }

    /**
     * Canvas height property double property.
     *
     * @return the double property
     */
    public DoubleProperty canvasHeightProperty() {
        return canvasHeight;
    }

    /**
     * Gets canvas width.
     *
     * @return the canvas width
     */
    public double getCanvasWidth() {
        return canvasWidth.get();
    }

    /**
     * Sets canvas width.
     *
     * @param width the width
     */
    public void setCanvasWidth(double width) {
        canvasWidth.set(width);
    }

    /**
     * Gets canvas height.
     *
     * @return the canvas height
     */
    public double getCanvasHeight() {
        return canvasHeight.get();
    }

    /**
     * Sets canvas height.
     *
     * @param height the height
     */
    public void setCanvasHeight(double height) {
        canvasHeight.set(height);
    }

}
