package com.paint.model;

import javafx.scene.paint.Color;

// Hold info about currently selected brush, image, color, shape, etc. settings.
public class PaintStateModel {
    // Create a nested class to allow for default values in obj creation
    private static class BrushObj {
        private String brushType;

        public BrushObj() {
            this.brushType = "StLine";
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
    private String currentToolType = "Select"; // Differentiates the differing tool types. i.e. (Select, brush, shape, etc.)

    public PaintStateModel() {
        this.currentBrush = new BrushObj();
        this.currentPaintColor = Color.BLACK; // Default color
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
