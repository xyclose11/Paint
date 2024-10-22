package com.paint.resource;

import com.paint.controller.CanvasController;
import com.paint.handler.WorkspaceHandler;
import javafx.beans.binding.Bindings;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;


/**
 * This class extends Group and allows any JavaFX 22 Node to be Transformable.
 *
 * NOTE: Transformable in this application currently only supports:
 * - Translation ((X,Y) Movement)
 *
 * @since 1.5
 * */
public class TransformableNode extends Group {
	private Node originalNode;
	private Rectangle selectionRect;
	private boolean isTransformable = false;
	private final WorkspaceHandler workspaceHandler;
	private double startX;
	private double startY;
	private final CanvasController canvasController;

	/**
	 *
	 * @param node currently supported Nodes include, extensions from Shape, TextArea, Canvas, Selection
	 * */
	public TransformableNode(Node node, WorkspaceHandler workspaceHandler) {
		super(node);
		this.originalNode = node;
		this.workspaceHandler = workspaceHandler;
		this.canvasController = workspaceHandler.getCurrentWorkspace().getCanvasController();
		createSelectionBox();
	}

	private void handleKeyPress(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			exitTransformMode();
			event.consume();
		}
	}

	/**
	 * This method enables Transformations on a TransformableNode, which allows the user to interact with the Node via
	 * transformations.
	 * NOTE: This method does setup multiple Events, which get removed at the end of the TransformableNodes'
	 * lifecycle
	 * */
	public void enableTransformations() {
		if (!isTransformable) {
			return;
		}

		Pane parentPane = this.canvasController.getDrawingPane();

		if (parentPane.getScene() != null) {
			parentPane.getScene().setOnKeyPressed(this::handleKeyPress);
		}

		// Translation handler (XY Movement) SECTION START
		parentPane.setOnMousePressed(this::handleMousePressed);

		this.setOnMouseDragged(dragEvent -> {
			// Update shape position
			this.setTranslateX((dragEvent.getSceneX() - startX));
			this.setTranslateY((dragEvent.getSceneY() - startY));
		});

		this.workspaceHandler.getPaintStateModel().setCurrentNode(this);
		// Translation handler (XY Movement) SECTION END
	}

	private void handleMousePressed(MouseEvent mouseEvent) {
		if (!this.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
			exitTransformMode();
		}else {
			if (this.originalNode instanceof ImageView) {
				this.workspaceHandler.getPaintStateModel().getImageView().translateXProperty().bind(this.translateXProperty());
				this.workspaceHandler.getPaintStateModel().getImageView().translateYProperty().bind(this.translateYProperty());
			}
			if (this.originalNode instanceof TextArea) {
				// Handle TextTool
				startX = mouseEvent.getX();
				startY = mouseEvent.getY();
				return;
			}
			startX = mouseEvent.getSceneX();
			startY = mouseEvent.getSceneY();
		}

	}

	/**
	 * This method exits a given TransformableNode from Transformation-Mode.
	 * This removes all event listeners and handlers that were added in the 'enableTransformations' method.
	 * Also removes the Selection-Rectangle (Dashed outline), unbinds properties, and calls appropriate
	 * methods to apply the Node to the canvas based on its original node type
	 * NOTE: This method enables the event listeners in the current workspaces' canvasController instance
	 * */
	public void exitTransformMode() {
		isTransformable = false;
		Pane parentPane = this.canvasController.getDrawingPane();

		this.setOnMouseDragged(null);
		this.setOnMousePressed(null);
		this.setOnMouseEntered(null);
		this.setOnKeyPressed(null);

		parentPane.setOnMousePressed(null);
		if (parentPane.getScene() != null) {
			parentPane.getScene().setOnKeyPressed(null);
		}
		// Remove selectionRectangle
        this.getChildren().remove(selectionRect);

		// Convert shape -> canvas
		// Check if current object is a shape

		String currentToolType = this.workspaceHandler.getPaintStateModel().getCurrentToolType();
		switch (currentToolType) {
			case ("shape"):
				this.canvasController.applyPaneShapeToCanvas((Shape) this.getChildren().get(0));
				break;
			case ("selection"), ("paste"):
				this.canvasController.applySelectionToCanvas((ImageView) this.originalNode);
				break;
		}

		selectionRect.heightProperty().unbind();
		selectionRect.widthProperty().unbind();
		selectionRect.xProperty().unbind();
		selectionRect.yProperty().unbind();


		// Enable CanvasController handlers
		canvasController.setCanvasDrawingStackPaneHandlerState(true);

		this.workspaceHandler.getPaintStateModel().setCurrentNode(null);
	}


	private void createSelectionBox () {
		// Create a rect that will surround the currentShape
		this.selectionRect = new Rectangle();
		this.selectionRect.setId("selectionBox");

		// Bind the rectangle's position to the current shape's bounds in parent
		this.selectionRect.xProperty().bind(
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

		this.getChildren().add(selectionRect);

		this.setOnMouseEntered(mouseE -> {
			this.setCursor(Cursor.MOVE);
		});
	}

	/**
	 * <p>
	 * This method rotates the node 90 degrees to the right.
	 *
	 * <p>
	 * NOTE: This utilizes JavaFX 22 Shape class's. Not GraphicsContext from Canvas
	 *
	 * */
	public void rotate90Right() {
		// get previous rotation
		double prevRotation = this.getRotate();
		this.setRotate(prevRotation + 90);
	}

	/**
	 * <p>
	 * This method rotates the node 90 degrees to the left.
	 *
	 * <p>
	 * NOTE: This utilizes JavaFX 22 Shape class's. Not GraphicsContext from Canvas
	 *
	 * */
	public void rotate90Left() {
		double prevRotation = this.getRotate();
		this.setRotate(prevRotation - 90);
	}

	/**
	 * <p>
	 * This method rotates the node 180 degrees from its current location.
	 *
	 * <p>
	 * NOTE: This utilizes JavaFX 22 Shape class's. Not GraphicsContext from Canvas
	 *
	 * */
	public void rotate180() {
		if (this.getRotate() == 180) {
			this.setRotate(0);
		} else {
			this.setRotate(180);
		}
	}

	/**
	 * This method determines whether the Original Node (Node passed when first creating the
	 * TransformableNode) is an instance of the 'Shape' class
	 *
	 * @return boolean True if the original node is a shape, False otherwise
	 * */
	public boolean isShape() {
		// Check if originalNode is a shape
        return originalNode instanceof Shape;
	}

	/**
	 * Gets original node.
	 *
	 * @return the original node
	 */
	public Node getOriginalNode() {
		return originalNode;
	}

	/**
	 * Sets original node.
	 *
	 * @param originalNode the original node
	 */
	public void setOriginalNode(Node originalNode) {
		this.originalNode = originalNode;
	}

	/**
	 * Is transformable boolean.
	 *
	 * @return the boolean
	 */
	public boolean isTransformable() {
		return isTransformable;
	}

	/**
	 * Sets transformable.
	 *
	 * @param transformable the transformable
	 */
	public void setTransformable(boolean transformable) {
		isTransformable = transformable;
	}

	/**
	 * This method takes the TransformableNodes' startX and {@code translateX()} and returns the SUM
	 * @return the combination of StartX + TranslateX
	 * */
	public double getTranslatedX () {
		double x = this.getTranslateX() + this.startX;
		return x;
	}

	/**
	 * This method takes the TransformableNodes' startY and {@code translateY()} and returns the SUM
	 * @return the combination of StartY + TranslateY
	 * */
	public double getTranslatedY () {
		double y = this.getTranslateY() + this.startY;
		return y;
	}
}
