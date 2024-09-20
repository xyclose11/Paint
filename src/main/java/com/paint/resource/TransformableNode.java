package com.paint.resource;

import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class TransformableNode extends Group {
	private Node originalNode;
	private Rectangle selectionRect;

	// creates a transformableNode around a previous node
	public TransformableNode(Node node) {
		super(node);
		this.originalNode = node;
		createSelectionBox();
	}


	private void createSelectionBox () {
		// Create a rect that will surround the currentShape
		selectionRect = new Rectangle();
		selectionRect.setId("selectionBox");

		// Bind the rectangle's position to the current shape's bounds in parent
		selectionRect.xProperty().bind(
				Bindings.createDoubleBinding(() -> originalNode.getBoundsInParent().getMinX(),
						originalNode.boundsInParentProperty())
		);
		selectionRect.yProperty().bind(
				Bindings.createDoubleBinding(() -> originalNode.getBoundsInParent().getMinY(),
						originalNode.boundsInParentProperty())
		);

		selectionRect.widthProperty().bind(
				Bindings.createDoubleBinding(() -> originalNode.getBoundsInParent().getWidth(),
						originalNode.boundsInParentProperty())
		);
		selectionRect.heightProperty().bind(
				Bindings.createDoubleBinding(() -> originalNode.getBoundsInParent().getHeight(),
						originalNode.boundsInParentProperty())
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


		this.getChildren().addAll(selectionRect, originalNode);

		this.setOnMouseEntered(mouseE -> {
			this.setCursor(Cursor.MOVE);
		});

		// TODO remove selectionbox when saving since it willcapture the selectionbox in the file

	}

	public Node getOriginalNode() {
		return originalNode;
	}

	public void setOriginalNode(Node originalNode) {
		this.originalNode = originalNode;
	}
}
