package com.paint.model;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;

import java.util.Objects;

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
    private StrokeLineCap currentStrokeLineCap;
    private double currentShapeLineStrokeWidth;
    private boolean isTransformable;
    private Group shapeTransformationGroup;

    public PaintStateModel() {
        this.currentBrush = new BrushObj();
        this.currentPaintColor = Color.BLACK; // Default color
        this.currentShape = null;
        this.currentLineWidth = 1.0;
        this.currentStrokeLineCap = StrokeLineCap.ROUND; // Default cap for lines
        this.currentShapeLineStrokeWidth = 1.0;
        this.isTransformable = false;
        this.shapeTransformationGroup = new Group();
    }

    public Group getShapeTransformationGroup() {
        return shapeTransformationGroup;
    }

    public void setShapeTransformationGroup(Group shapeTransformationGroup) {
        this.shapeTransformationGroup = shapeTransformationGroup;
    }

    public boolean isTransformable() {
        return isTransformable;
    }

    public void setTransformable(boolean transformable) {
        isTransformable = transformable;

        if (transformable && currentShape != null) {
            // Add dashed outline
//            createSelectionBox(this.currentShape.getParent().getLayoutBounds(), this.currentShape); // TODO add dashed outline

            // Add event listeners

            // Set keybinding for ESC to exit transform mode
            this.currentShape.getParent().getScene().setOnKeyPressed(keyEvent -> {
                // Check key type
                if (Objects.equals(keyEvent.getCode().getName(), "Esc")) {
                    // Exit transformation mode
                    this.setTransformable(false);
                }
                this.currentShape.getParent().getScene().setOnKeyPressed(null);
            });

            // Translation handler (XY Movement)
            this.currentShape.setOnMousePressed(e -> {
                // Listen for drag
                this.currentShape.setOnMouseDragged(d -> {

                    if (this.currentShape instanceof Rectangle rect) {
                        rect.setX(d.getSceneX() - rect.getX());
                        rect.setY(d.getSceneY() - rect.getY());
                    }
                });

            });

            // Check if user clicks outside the border -> enter shapePlacement mode
            this.currentShape.setOnMouseExited(e -> {
                this.currentShape.getParent().setOnMousePressed(mouseEvent -> {
                    if (!this.currentShape.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                        // User clicked off of the selected shape
                        this.setTransformable(false);
                    }
                    this.currentShape.getParent().setOnMouseClicked(null);
                });

                this.currentShape.setPickOnBounds(false);
                this.currentShape.setOnMouseExited(null);
            });
        }
    }

    private void createSelectionBox (Bounds parentBounds, Shape currentShape) {
        // Get dimensions of the bounding box from parent
        double xMin = parentBounds.getMinX();
        double xMax = parentBounds.getMaxX();

        double yMin = parentBounds.getMinY();
        double yMax = parentBounds.getMaxY();

        // Create a rect that will surround the currentShape
        Rectangle selectionRect = new Rectangle(xMin, xMax, yMin, yMax);

        selectionRect.getStrokeDashArray().addAll(20.0);
        selectionRect.setStrokeDashOffset(10);

//        currentShape.set
    }

    public double getCurrentShapeLineStrokeWidth() {
        return currentShapeLineStrokeWidth;
    }

    public void setCurrentShapeLineStrokeWidth(double currentShapeLineStrokeWidth) {
        this.currentShapeLineStrokeWidth = currentShapeLineStrokeWidth;
    }

    public StrokeLineCap getCurrentStrokeLineCap() {
        return currentStrokeLineCap;
    }

    public void setCurrentStrokeLineCap(StrokeLineCap currentStrokeLineCap) {
        this.currentStrokeLineCap = currentStrokeLineCap;
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
