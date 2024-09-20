package com.paint.resource;

import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class TransformableShape extends Shape {
    private Group shapeTransformationGroup;

    public TransformableShape() {
        super();
    }

    private void createSelectionBox (Shape currentShape, Pane drawing) {
        // Create a rect that will surround the currentShape
        Rectangle selectionRect = new Rectangle();
        selectionRect.setId("selectionBox");

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

        selectionRect.getStrokeDashArray().addAll(9.5);
        selectionRect.setFill(Color.TRANSPARENT);
        selectionRect.setStroke(Color.GRAY);
        selectionRect.setStrokeDashOffset(40);
        selectionRect.setStrokeType(StrokeType.OUTSIDE);
        selectionRect.toFront();

        // TEST for the impl of other transformations (scale/resize, rotate, etc) | need to work out the event handlers
//        Rectangle middleBox = new Rectangle();
//        middleBox.xProperty().bind(selectionRect.xProperty().add(selectionRect.widthProperty().subtract(middleBox.widthProperty()).divide(2)));
//        middleBox.yProperty().bind(selectionRect.yProperty());
//        middleBox.setWidth(10);
//        middleBox.setHeight(10);
//
//        middleBox.xProperty().addListener(((observable, oldValue, newValue) -> updateSelection(selectionRect, middleBox)));
//        middleBox.yProperty().addListener(((observable, oldValue, newValue) -> updateSelection(selectionRect, middleBox)));
//        middleBox.heightProperty().addListener(((observable, oldValue, newValue) -> updateSelection(selectionRect, middleBox)));
//
//        middleBox.setFill(Color.ORANGE);
//        middleBox.setStroke(Color.ORANGE);
//
//        middleBox.setPickOnBounds(true);
        // END TEST

        // Setup mouse event handlers
//        selectionRect.setOnMouseEntered(mouseEvent -> {
//            selectionRect.setCursor(Cursor.H_RESIZE);
//        });

        Group group = new Group(selectionRect, currentShape);
        setShapeTransformationGroup(group);

        shapeTransformationGroup.setOnMouseEntered(mouseE -> {
            shapeTransformationGroup.setCursor(Cursor.MOVE);
        });

        // TODO remove selectionbox when saving since it willcapture the selectionbox in the file

        drawing.getChildren().add(shapeTransformationGroup);
    }

    public Group getShapeTransformationGroup() {
        return shapeTransformationGroup;
    }


    public void setShapeTransformationGroup(Group shapeTransformationGroup) {
        this.shapeTransformationGroup = shapeTransformationGroup;
    }
}
