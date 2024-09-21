package com.paint.resource;

import com.paint.controller.CanvasController;
import com.paint.handler.WorkspaceHandler;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import java.util.Objects;

public class TransformableNode extends Group {
	private Node originalNode;
	private Rectangle selectionRect;
	private boolean isTransformable;
	private WorkspaceHandler workspaceHandler;
	private double startX;
	private double startY;
	private CanvasController canvasController;

	// creates a transformableNode around a previous node
	public TransformableNode(Node node, WorkspaceHandler workspaceHandler) {
		super(node);
		this.originalNode = node;
		this.workspaceHandler = workspaceHandler;
		this.canvasController = workspaceHandler.getCurrentWorkspace().getCanvasController();
		createSelectionBox();
	}

	public void enableTransformations() {

		isTransformable = true;

		Pane parentPane = this.canvasController.getDrawingPane();

		parentPane.getScene().addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ESCAPE) {
					event.consume();
					exitTransformMode();
				}
			}
		});
		// Translation handler (XY Movement) SECTION START

		parentPane.setOnMousePressed(this::handleMousePressed);

		this.setOnMouseDragged(dragEvent -> {
			// Update shape position
			this.setTranslateX((dragEvent.getSceneX() - startX));
			this.setTranslateY((dragEvent.getSceneY() - startY));
		});

		this.workspaceHandler.getPaintStateModel().setCurrentShape(this);

		// Translation handler (XY Movement) SECTION END

		// Resize handler SECTION START
		// Resize handler SECTION END
	}

	private void handleMousePressed(MouseEvent mouseEvent) {
		if (!this.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
			exitTransformMode();
		}else {
			if (Objects.equals(this.workspaceHandler.getPaintStateModel().getCurrentToolType(), "selection")) {
				this.workspaceHandler.getPaintStateModel().getImageView().translateXProperty().bind(this.translateXProperty());
				this.workspaceHandler.getPaintStateModel().getImageView().translateYProperty().bind(this.translateYProperty());
			}
			startX = mouseEvent.getSceneX();
			startY = mouseEvent.getSceneY();
		}

	}

	public void exitTransformMode() {
		isTransformable = false;
		this.setOnMouseDragged(null);
		this.setOnMousePressed(null);
		this.setOnMouseEntered(null);
		Pane parentPane = this.canvasController.getDrawingPane();
		System.out.println("CONTROLLER: " + this.canvasController);
		parentPane.setOnMousePressed(null);
		// Remove selectionRectangle
		if (!this.getChildren().isEmpty()) {
			this.getChildren().get(1).setVisible(false);
		}
		// Convert shape -> canvas
		// Check if current object is a shape
		if (Objects.equals(this.workspaceHandler.getPaintStateModel().getCurrentToolType(), "shape")) { // TODO convert this into a switch/case statement
			System.out.println("TOOL");
			System.out.println(this.workspaceHandler.getPaintStateModel().getCurrentShape());
			this.workspaceHandler.getPaintStateModel().setCurrentShape(this);
			this.canvasController.applyPaneShapeToCanvas((Shape) this.getChildren().get(0));
		} else if (Objects.equals(this.workspaceHandler.getPaintStateModel().getCurrentToolType(), "selection")) {
			// Remove outer selection rectangle
			this.canvasController.applySelectionToCanvas(this.workspaceHandler.getPaintStateModel().getImageView());
		}


		// Enable CanvasController handlers
		canvasController.setCanvasDrawingStackPaneHandlerState(true);
		// Notify handler that user is no longer editing
//		this.workspaceHandler.setEditing(false);
		// TODO figure out a way to exit user from transform mode when switching tabs
		// LAST WORKING ON IMPLEMENTING THE REST OF THE SHAPES
		// CURRENT ISSUE IS THAT CURRENTSHAPE IS NOT BEING UPDATED CORRECTLY
		// TODO CHANGE NAME OF CURRENTSHAPE TO SOMETHING BETTER
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

		this.getChildren().addAll(selectionRect);

		this.setOnMouseEntered(mouseE -> {
			this.setCursor(Cursor.MOVE);
		});

		// TODO remove selectionbox when saving since it willcapture the selectionbox in the file

	}

	public boolean isShape() {
		// Check if originalNode is a shape
        return originalNode instanceof Shape;
	}
	public Node getOriginalNode() {
		return originalNode;
	}

	public void setOriginalNode(Node originalNode) {
		this.originalNode = originalNode;
	}

	public boolean isTransformable() {
		return isTransformable;
	}

}
