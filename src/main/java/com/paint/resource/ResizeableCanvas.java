package com.paint.resource;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

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

		//		adjustResolutionOnRotation();
	}

	public void rotate90Left() {
		double rotateAmount = (this.getRotate() - 90) % 360;
		if (rotateAmount < 0) {
			rotateAmount += 360;
		}
		this.setRotate(rotateAmount);
//		adjustResolutionOnRotation();
	}

	public void rotate180() {
		double rotateAmount = this.getRotate() + 180;
		this.setRotate(rotateAmount);
	}

	private void adjustResolutionOnRotation() {
			originalHeight = this.getHeight();
			originalWidth = this.getWidth();
			this.setWidth(originalHeight);
			this.setHeight(originalWidth);
	}

	public void verticalFlip() {
		WritableImage writableImage = new WritableImage((int)(this.getWidth()), (int) (this.getHeight()));
		this.snapshot(null, writableImage);

		this.getGraphicsContext2D().drawImage(writableImage, 0, 0, writableImage.getWidth(), writableImage.getHeight(), writableImage.getWidth(), 0, -writableImage.getWidth(), writableImage.getHeight());
	}

	public void horizontalFlip() {
		WritableImage writableImage = new WritableImage((int)(this.getWidth()), (int) (this.getHeight()));
		this.snapshot(null, writableImage);

		this.getGraphicsContext2D().drawImage(writableImage, 0, 0, writableImage.getWidth(), writableImage.getHeight(), 0, writableImage.getHeight(), writableImage.getWidth(), -writableImage.getHeight());
	}




}
