package com.paint.controller;

import com.paint.model.PaintStateModel;
import com.paint.resource.ResizeableCanvas;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
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
		selectionRect.getStrokeDashArray().setAll(2d, 10d);
		selectionRect.setStroke(Color.GRAY);
		selectionRect.setFill(Color.TRANSPARENT);
		selectionRect.setStrokeType(StrokeType.OUTSIDE);

		// Check if cursor is going in Quadrant 4 (Meaning that it doesn't require any calculation swaps)
		if (curX >= startX && curY >= startY) {
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
		}	}

	public void handleSelectionReleased() {
		// On release transfer everything inside the rect into a new draggable rect
		WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
		canvas.snapshot(null, image);

		PixelReader pixelReader = image.getPixelReader();
		// Take a snapshot within the selection box

		WritableImage selectImg = new WritableImage(pixelReader, (int) startX, (int) startY, (int) selectionRect.getWidth(), (int) selectionRect.getHeight());

		ImageView imageView = new ImageView(selectImg);
		imageView.setX(startX);
		imageView.setY(startY);

		// Remove a rect of same size from canvas
		canvas.getGraphicsContext2D().clearRect(startX, startY, selectionRect.getWidth(), selectionRect.getHeight());

		this.paintStateModel.setCurrentShape(selectionRect);
		this.paintStateModel.setCurrentSelection(imageView);

		this.drawingPane.getChildren().add(imageView);

		// Enable transformations
		this.paintStateModel.setTransformable(true, drawingPane);

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
}
