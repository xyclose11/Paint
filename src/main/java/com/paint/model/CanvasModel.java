package com.paint.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class CanvasModel {
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();

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
