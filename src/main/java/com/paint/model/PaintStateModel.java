package com.paint.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

// Hold info about currently selected brush, image, color, shape, etc. settings.
public class PaintStateModel {
    // Create a nested class to allow for default values in obj creation
    private static class BrushObj {
        private String brushType;

        public BrushObj() {
            this.brushType = "regular";
        }

        public BrushObj(String brushType) {
            this.brushType = brushType;
        }
        public String getBrushType() {
            return brushType;
        }

        public void setBrushType(String brushType) {
            this.brushType = brushType;
        }

    }

    private Color currentPaintColor;

    private final BrushObj currentBrush;
    private String currentTool = "StLine"; // Holds the currentTool that the user has selected. // TODO Change to selection tool when impl
    private String currentToolType = "shape"; // Differentiates the differing tool types. i.e. (Select, brush, shape, etc.)
    private Shape currentShape;
    private double currentLineWidth;

    public PaintStateModel() {
        this.currentBrush = new BrushObj();
        this.currentPaintColor = Color.BLACK; // Default color
        this.currentShape = null;
        this.currentLineWidth = .5;
    }

    public double getCurrentLineWidth() {
        return currentLineWidth;
    }

    public void setCurrentLineWidth(double currentLineWidth) {
        this.currentLineWidth = currentLineWidth;
    }

    public Shape getCurrentShape() {
        return this.currentShape;
    }

    public void setCurrentShape(Shape currentShape) {
        this.currentShape = currentShape;
    }

    public String getCurrentTool() {
        return this.currentTool;
    }

    public void setCurrentTool(String currentTool) {
        this.currentTool = currentTool;
    }

    public String getCurrentBrush() {
        return currentBrush.getBrushType();
    }

    public void setCurrentBrush(String currentBrush) {
        // TODO add validation for the brush type
        this.currentBrush.setBrushType(currentBrush);
    }

    public Color getCurrentPaintColor() {
        return currentPaintColor;
    }

    public void setCurrentPaintColor(Color color) {
        this.currentPaintColor = color;
    }

    public String getCurrentToolType() {
        return currentToolType;
    }

    public void setCurrentToolType(String currentToolType) {
        this.currentToolType = currentToolType;
    }


}
