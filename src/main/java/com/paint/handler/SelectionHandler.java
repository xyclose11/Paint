package com.paint.handler;

import com.paint.model.PaintStateModel;
import com.paint.resource.ResizeableCanvas;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

// Defaults to rect selection // Disregard 'free-form' version
public class SelectionHandler {
	private Group canvasGroup;
	private Canvas canvas;
	private Pane drawingPane;
	private Rectangle selectionRect;
	private final Clipboard clipboard = Clipboard.getSystemClipboard();
	private final ClipboardContent clipboardContent = new ClipboardContent();
	private ImageView selectionImage;
	private WorkspaceHandler workspaceHandler;

	public WorkspaceHandler getCurrentWorkspaceModel() {
		return workspaceHandler;
	}

	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	private PaintStateModel paintStateModel;

	public void setPaintStateModel(PaintStateModel paintStateModel) {
		this.paintStateModel = paintStateModel;
	}

	private double startX, startY;

	public void handleSelectionPressed(double x, double y) {
		// Save starting x,y
		startX = x;
		startY = y;

		Rectangle rectangle = new Rectangle(x,y, 1, 1);

		selectionRect = rectangle;

		this.drawingPane.getChildren().add(rectangle);
	}

	public void handleSelectionDragged(double curX, double curY) {
		applySelectionRectAttributes(selectionRect);

		/*
			   2   |   1
			-------|------
			   3   |   4
		*/


		// Check if cursor is going in Quadrant 4 (Meaning that it doesn't require any calculation swaps)
		if (curX >= startX && curY >= startY) { // TODO replace this when you integrate with a shapeManager
			selectionRect.setWidth((curX - startX));
			selectionRect.setHeight((curY - startY));
			return;
		}

		// Check if cursor is in Quadrants 2 OR 3
		if (curX < startX) {
			// Swap calculation setters
			selectionRect.setX(curX);
			selectionRect.setWidth(Math.abs(startX - curX));
			selectionRect.setHeight(Math.abs(curY - startY));
		}

		// Check if cursor is in Quadrants 2 OR 1
		if (curY < startY) {
			// Swap calculation setters
			selectionRect.setY(curY);
			selectionRect.setWidth(Math.abs(curX - startX));
			selectionRect.setHeight(Math.abs(startY - curY));
		}
	}

	public void handleSelectionReleased() {
		// On release transfer everything inside the rect into a new draggable rect
		WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
		canvas.snapshot(null, image);

		PixelReader pixelReader = image.getPixelReader();

		int selectX = (int) selectionRect.getX();
		int selectY = (int) selectionRect.getY();


		WritableImage selectImg = new WritableImage(pixelReader, selectX, selectY, (int) selectionRect.getWidth(), (int) selectionRect.getHeight());

		ImageView imageView = new ImageView(selectImg);
		imageView.setX(selectX);
		imageView.setY(selectY);

		// Remove a rect of same size from canvas
		canvas.getGraphicsContext2D().clearRect(selectX, selectY, selectionRect.getWidth(), selectionRect.getHeight());

		this.paintStateModel.setCurrentShape(selectionRect);
		this.paintStateModel.setCurrentSelection(imageView);

		this.drawingPane.getChildren().add(imageView);

		// Enable transformations
		this.paintStateModel.setTransformable(true, drawingPane);
	}

	private void applySelectionRectAttributes(Rectangle rectangle) {
		rectangle.getStrokeDashArray().setAll(2d, 10d);
		rectangle.setStroke(Color.GRAY);
		rectangle.setFill(Color.TRANSPARENT);
		rectangle.setStrokeType(StrokeType.OUTSIDE);
	}


	public void copySelectionContent() {
		// Check if there is a selection made
		if (this.paintStateModel.getImageView() == null) {
			return;
		}

		// Copies the selection content into an image on the systems clipboard
		try {
			clipboardContent.putImage(this.paintStateModel.getImageView().getImage());
			clipboard.setContent(clipboardContent);
		} catch (Exception e) {
			Alert copyError = new Alert(Alert.AlertType.ERROR, "ERROR COPYING SELECTION. PLEASE TRY AGAIN: " + e.getMessage());
			copyError.show();
			e.getMessage();
		}
	}

	public void pasteClipboardImage() {
		// System clipboard IMG -> canvas selection
		Image image = Clipboard.getSystemClipboard().getImage();

		if (image != null) {
			double width = image.getWidth();
			double height = image.getHeight();
			double x = 0;
			double y = 0;

			// Create selection Rectangle
			this.selectionRect = new Rectangle(x,y,width,height);
			applySelectionRectAttributes(selectionRect);

			this.drawingPane.getChildren().add(this.selectionRect);

			PixelReader pixelReader = image.getPixelReader();

			WritableImage selectImg = new WritableImage(pixelReader, 0, 0,(int) image.getWidth(), (int) image.getHeight());

			ImageView imageView = new ImageView(selectImg);

//			this.paintStateModel.setCurrentShape(selectionRect);
			this.paintStateModel.setCurrentSelection(imageView);

			this.workspaceHandler.getCurrentWorkspace().getCanvasController().getDrawingPane().getChildren().add(imageView);
//			this.currentWorkspaceModel.getCurrentWorkspace().getCanvasController().getDrawingPane().getChildren().add(selectionRect);


			// Enable transformations
			this.paintStateModel.setTransformable(true, this.drawingPane);
		}
	}

	public Group getCanvasGroup() {
		return canvasGroup;
	}

	public void setCanvasGroup(Group canvasGroup) {
		this.canvasGroup = canvasGroup;

		StackPane drawingContainer = (StackPane) canvasGroup.getChildren().get(0);

		// Split into canvas & pane
		this.canvas = (ResizeableCanvas) drawingContainer.getChildren().get(0);
		this.drawingPane = (Pane) drawingContainer.getChildren().get(1);
	}

	public void removeSelectionRectangle() {
		this.drawingPane.getChildren().clear();
	}
}
