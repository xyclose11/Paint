package com.paint.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;

// Model to hold canvas information
public class CanvasModel {
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();
    private Group canvasGroup;

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
