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
}
