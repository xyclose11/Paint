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

    public PaintStateModel() {
        this.currentBrush = new BrushObj();
        this.currentPaintColor = Color.BLACK; // Default color
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
}
