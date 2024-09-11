package com.paint.resource;

import javafx.scene.canvas.Canvas;

public class ResizeableCanvas extends Canvas {

	public ResizeableCanvas() {
		super();

	}

	public ResizeableCanvas(double width, double height) {
		super(width, height);
		widthProperty().addListener(((observable, oldValue, newValue) -> {

		}));
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public void resize(double width, double height) {
		super.setWidth(width);
		super.setHeight(height);
	}

	public void expandLeft(double newWidth) {
		// Ensure the new width is positive
		if (newWidth > 0) {
			this.setWidth(newWidth);
		}

		// Optionally update the height if needed
		// mainCanvas.setHeight(newHeight);

		System.out.println("Resized Width: " + newWidth);
	}
}
