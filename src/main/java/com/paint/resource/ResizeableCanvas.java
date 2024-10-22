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


	/**
	 * Instantiates a new Resizeable canvas.
	 */
	public ResizeableCanvas() {
		super();
	}

	/**
	 * Instantiates a new Resizeable canvas.
	 *
	 * @param width  the width
	 * @param height the height
	 */
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

	/**
	 * Expand w.
	 *
	 * @param newWidth the new width
	 */
	public void expandW(double newWidth) {
		// Ensure the new width is positive
		if (newWidth > 0) {
			this.setWidth(newWidth);
		}
	}

	/**
	 * Expand e.
	 *
	 * @param newWidth the new width
	 */
	public void expandE(double newWidth) {
		// Ensure the new width is positive
		if (newWidth > 0) {
			this.setWidth(newWidth);
		}
	}

	/**
	 * Expand n.
	 *
	 * @param newHeight the new height
	 */
	public void expandN(double newHeight) {
		// Ensure the new width is positive
		if (newHeight > 0) {
			this.setHeight(newHeight);
		}
	}

	/**
	 * Expand s.
	 *
	 * @param newHeight the new height
	 */
	public void expandS(double newHeight) {
		// Ensure the new width is positive
		if (newHeight > 0) {
			this.setHeight(newHeight);
		}
	}
	/**
	 *
	 * <p>
	 *     This method rotates the canvas by 90 degrees to the right.*
	 * <p>
	 * General Process:
	 * <ul>
	 *     <li>
	 *         <h4>
	 *             1.
	 *         </h4>
	 *         Create a new writable image of the current canvas
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             2.
	 *         </h4>
	 *         call {@code adjustResolutionOnRotation()} to adjust the canvas resolution before applying rotation
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             3.
	 *         </h4>
	 *         Save the graphics context apply translations, rotate and apply the writable image from step 1
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             4.
	 *         </h4>
	 *         Restore graphics context
	 *     </li>
	 * </ul>
	 *
	 * NOTE: This process does involve the usage of 'canvas.snapshot' to ensure that when rotated if the user were to draw \n
	 * it wouldn't rotate the drawing.
    * */
	public void rotate90Right() {
		WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
		this.snapshot(null, writableImage);
		adjustResolutionOnRotation();

		this.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		this.getGraphicsContext2D().save();

		this.getGraphicsContext2D().translate(this.getWidth(), 0);
		this.getGraphicsContext2D().rotate(90);
		this.getGraphicsContext2D().drawImage(writableImage, 0, 0);

		this.getGraphicsContext2D().restore();

	}
	/**
	 *
	 * <p>
	 *     This method rotates the canvas by 90 degrees to the left.
	 *
	 * <p>
	 * General Process:
	 * <ul>
	 *     <li>
	 *         <h4>
	 *             1.
	 *         </h4>
	 *         Create a new writable image of the current canvas
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             2.
	 *         </h4>
	 *         call {@code adjustResolutionOnRotation()} to adjust the canvas resolution before applying rotation
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             3.
	 *         </h4>
	 *         Save the graphics context apply translations, rotate and apply the writable image from step 1
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             4.
	 *         </h4>
	 *         Restore graphics context
	 *     </li>
	 * </ul>
	 *
	 * NOTE: This process does involve the usage of 'canvas.snapshot' to ensure that when rotated if the user were to draw \n
	 * it wouldn't rotate the drawing.
	 * */
	public void rotate90Left() {
		WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
		this.snapshot(null, writableImage);
		adjustResolutionOnRotation();

		this.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		this.getGraphicsContext2D().save();

		this.getGraphicsContext2D().translate(0, this.getHeight());
		this.getGraphicsContext2D().rotate(-90);
		this.getGraphicsContext2D().drawImage(writableImage, 0, 0);

		this.getGraphicsContext2D().restore();

	}

	/**
	 *
	 * <p>
	 *     This method rotates the canvas by 180 degrees.
	 *
	 * <p>
	 * General Process:
	 * <ul>
	 *     <li>
	 *         <h4>
	 *             1.
	 *         </h4>
	 *         Create a new writable image of the current canvas
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             2.
	 *         </h4>
	 *         call {@code adjustResolutionOnRotation()} to adjust the canvas resolution before applying rotation
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             3.
	 *         </h4>
	 *         Save the graphics context apply translations, rotate and apply the writable image from step 1
	 *     </li>
	 *     <li>
	 *         <h4>
	 *             4.
	 *         </h4>
	 *         Restore graphics context
	 *     </li>
	 * </ul>
	 *
	 * NOTE: This process does involve the usage of 'canvas.snapshot' to ensure that when rotated if the user were to draw \n
	 * it wouldn't rotate the drawing.
	 * */
	public void rotate180() {
		WritableImage writableImage = new WritableImage((int) this.getWidth(), (int) this.getHeight());
		this.snapshot(null, writableImage);

		this.getGraphicsContext2D().clearRect(0, 0, this.getWidth(), this.getHeight());
		this.getGraphicsContext2D().save();

		this.getGraphicsContext2D().translate(this.getWidth(), this.getHeight());
		this.getGraphicsContext2D().rotate(180);
		this.getGraphicsContext2D().drawImage(writableImage, 0, 0);

		this.getGraphicsContext2D().restore();
	}

	private void adjustResolutionOnRotation() {
			originalHeight = this.getHeight();
			originalWidth = this.getWidth();
			this.setWidth(originalHeight);
			this.setHeight(originalWidth);
	}

	/**
	 * Vertical flip.
	 */
	public void verticalFlip() {
		WritableImage writableImage = new WritableImage((int)(this.getWidth()), (int) (this.getHeight()));
		this.snapshot(null, writableImage);

		this.getGraphicsContext2D().drawImage(writableImage, 0, 0, writableImage.getWidth(), writableImage.getHeight(), writableImage.getWidth(), 0, -writableImage.getWidth(), writableImage.getHeight());
	}

	/**
	 * Horizontal flip.
	 */
	public void horizontalFlip() {
		WritableImage writableImage = new WritableImage((int)(this.getWidth()), (int) (this.getHeight()));
		this.snapshot(null, writableImage);

		this.getGraphicsContext2D().drawImage(writableImage, 0, 0, writableImage.getWidth(), writableImage.getHeight(), 0, writableImage.getHeight(), writableImage.getWidth(), -writableImage.getHeight());
	}




}
