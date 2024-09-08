package com.paint.model;

import com.paint.controller.CanvasController;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Translate;

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
    private Rectangle selectionRectangle;

    private CanvasController canvasController;

    public PaintStateModel() {
        this.currentBrush = new BrushObj();
        this.currentPaintColor = Color.BLACK; // Default color
        this.currentShape = null;
        this.currentLineWidth = 1.0;
        this.currentStrokeLineCap = StrokeLineCap.ROUND; // Default cap for lines
        this.currentShapeLineStrokeWidth = 1.0;
        this.isTransformable = false;
        this.shapeTransformationGroup = new Group();
        this.selectionRectangle = null;
    }

    public CanvasController getCanvasController() {
        return canvasController;
    }

    public void setCanvasController(CanvasController canvasController) {
        this.canvasController = canvasController;
    }

    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    public void setSelectionRectangle(Rectangle selectionRectangle) {
        this.selectionRectangle = selectionRectangle;
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

    public void setTransformable(boolean transformable, Pane draw) {
        isTransformable = transformable;

        if (transformable && currentShape != null) {
            // Add dashed outline
            createSelectionBox(this.currentShape, draw);

            // Add event listeners

            // Set keybinding for ESC to exit transform mode
            this.currentShape.getParent().getScene().setOnKeyPressed(keyEvent -> {
                // Check key type
                if (Objects.equals(keyEvent.getCode().getName(), "Esc")) {
                    // Exit transformation mode
                    this.setTransformable(false, null);

                    // Enable CanvasController handlers
                    this.canvasController.setCanvasDrawingStackPaneHandlerState(true);
                }
                this.currentShape.getParent().getScene().setOnKeyPressed(null);
            });

            // Translation handler (XY Movement)
            this.shapeTransformationGroup.setOnMousePressed(mousePressed -> {
                this.shapeTransformationGroup.setUserData(new double[]{mousePressed.getX(), mousePressed.getY()});
            });

            this.shapeTransformationGroup.setOnMouseDragged(d -> {
                double[] initialValues = (double[]) this.shapeTransformationGroup.getUserData();
                double startX = initialValues[0];
                double startY = initialValues[1];

                this.shapeTransformationGroup.setTranslateX(this.shapeTransformationGroup.getTranslateX() + (d.getX() - startX));
                this.shapeTransformationGroup.setTranslateY(this.shapeTransformationGroup.getTranslateY() + (d.getY() - startY));

                this.shapeTransformationGroup.setUserData(new double[]{d.getX(), d.getY()});
            });

            // Check if user clicks outside the border -> enter shapePlacement mode
            this.currentShape.setOnMouseExited(e -> {
                this.currentShape.getParent().getParent().setOnMousePressed(mouseEvent -> {
                    if (!this.currentShape.getParent().getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                        // User clicked off of the selected shape
                        this.setTransformable(false, null);

                        // Enable CanvasController handlers
                        this.canvasController.setCanvasDrawingStackPaneHandlerState(true);
                        this.shapeTransformationGroup.setOnMouseDragged(null);
                        this.currentShape.setPickOnBounds(false);
                    }
                    this.currentShape.getParent().setOnMouseClicked(null);
                });

                this.currentShape.setOnMouseExited(null);
            });
        } else {
            // Remove selectionRectangle
            if (this.shapeTransformationGroup.getChildren().size() >= 1) {
                this.shapeTransformationGroup.getChildren().get(0).setVisible(false);
            }
        }
    }

    private void createSelectionBox (Shape currentShape, Pane drawing) {
        // Create a rect that will surround the currentShape
        Rectangle selectionRect = new Rectangle();

        // Bind the rectangle's position to the current shape's bounds in parent
        selectionRect.xProperty().bind(
                Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getMinX(),
                        currentShape.boundsInParentProperty())
        );
        selectionRect.yProperty().bind(
                Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getMinY(),
                        currentShape.boundsInParentProperty())
        );

        selectionRect.widthProperty().bind(
                Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getWidth(),
                        currentShape.boundsInParentProperty())
        );
        selectionRect.heightProperty().bind(
                Bindings.createDoubleBinding(() -> currentShape.getBoundsInParent().getHeight(),
                        currentShape.boundsInParentProperty())
        );

        selectionRect.getStrokeDashArray().addAll(20.0);
        selectionRect.setFill(Color.TRANSPARENT);
        selectionRect.setStroke(Color.TURQUOISE);
        selectionRect.setStrokeDashOffset(10);
        selectionRect.toFront();



        // Setup mouse event handlers
        selectionRect.setOnMouseEntered(mouseEvent -> {
            selectionRect.setCursor(Cursor.MOVE);
        });

        Group group = new Group(selectionRect, currentShape);
        setShapeTransformationGroup(group);
        // Add transformation to the group
        Translate translate = new Translate();
        this.shapeTransformationGroup.getTransforms().add(translate);
        drawing.getChildren().add(shapeTransformationGroup);
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
