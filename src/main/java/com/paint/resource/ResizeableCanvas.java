package com.paint.resource;

import javafx.scene.canvas.Canvas;

/**
 * This class extends JavaFX 22 Canvas to override and add methods that will allow for resize
 * operations.
 *
 * @since 1.1
 * */
public class ResizeableCanvas extends Canvas {
	private double originalWidth;
	private double originalHeight;
	private double currentRotationAngle = 0; // Track the cumulative rotation


	public ResizeableCanvas() {
		super();
	}

	public ResizeableCanvas(double width, double height) {
		super(width, height);
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

	public void expandW(double newWidth) {
		// Ensure the new width is positive
		if (newWidth > 0) {
			this.setWidth(newWidth);
		}
	}

	public void expandE(double newWidth) {
		// Ensure the new width is positive
		if (newWidth > 0) {
			this.setWidth(newWidth);
		}
	}

	public void expandN(double newHeight) {
		// Ensure the new width is positive
		if (newHeight > 0) {
			this.setHeight(newHeight);
		}
	}

	public void expandS(double newHeight) {
		// Ensure the new width is positive
		if (newHeight > 0) {
			this.setHeight(newHeight);
		}
	}

	public void rotate90Right() {
		double rotateAmount = (this.getRotate() + 90) % 360;
		this.setRotate(rotateAmount);

		adjustResolutionOnRotation();
	}

	public void rotate90Left() {
		double rotateAmount = (this.getRotate() - 90) % 360;
		if (rotateAmount < 0) {
			rotateAmount += 360;
		}
		this.setRotate(rotateAmount);
		adjustResolutionOnRotation();

	}

	public void rotate180() {
		double rotateAmount = this.getRotate() + 180;
		this.setRotate(rotateAmount);
	}

	private void adjustResolutionOnRotation() {
		double currentRotation = this.getRotate() % 360;
		if (currentRotation % 180 == 90) { // Only adjust resolution for 90° or 270°
			originalHeight = this.getHeight();
			originalWidth = this.getWidth();
			this.setWidth(originalHeight);
			this.setHeight(originalWidth);
		}
	}



}
