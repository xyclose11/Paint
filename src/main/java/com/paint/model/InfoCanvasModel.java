package com.paint.model;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * This model is used to store the state of values that are displayed on the bottom most "information bar"
 *
 * @since 1.2
 * */
public class InfoCanvasModel {

	private Label resolutionLbl = new Label("");

	private Label selectionResLbl = new Label("");

	private Label mousePosLbl = new Label("");

	private Label currentLineWidthLbl = new Label("1px");

	/**
	 * Gets current line width lbl.
	 *
	 * @return the current line width lbl
	 */
	public Label getCurrentLineWidthLbl() {
		return currentLineWidthLbl;
	}

	/**
	 * Sets current line width lbl.
	 *
	 * @param lineWidth the line width
	 */
	public void setCurrentLineWidthLbl(double lineWidth) {
		this.currentLineWidthLbl.setText((int)lineWidth + "px");
	}

	/**
	 * Gets mouse pos lbl.
	 *
	 * @return the mouse pos lbl
	 */
// !!! Returning the Label object for binding purposes !!!
	public Label getMousePosLbl() {
		return mousePosLbl;
	}

	/**
	 * This method formats the {@code MouseEvent} current X and Y values to be displayed on the infobar.
	 *
	 * Format: 'X: 342 Y: 552'
	 *
	 * NOTE: This method uses {@code Math.round()} on both {@code MouseEvent.getX()} and {@code MouseEvent.getY()}
	 *
	 * @param mouseEvent MouseEvent from the current mouse position on the canvas
	 * */
	public void setMousePosLbl(MouseEvent mouseEvent) {
		mousePosLbl.setText("X: " + Math.round(mouseEvent.getX()) + " Y: " + Math.round(mouseEvent.getY()));
	}

	/**
	 * Gets resolution lbl.
	 *
	 * @return the resolution lbl
	 */
	public Label getResolutionLbl() {
		return resolutionLbl;
	}

	/**
	 * Sets resolution lbl text.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setResolutionLblText(double x, double y) {
		this.resolutionLbl.setText(x + " x " + y + "px");
	}

	/**
	 * Gets selection res lbl.
	 *
	 * @return the selection res lbl
	 */
	public Label getSelectionResLbl() {
		return selectionResLbl;
	}

	/**
	 * Sets selection res lbl.
	 *
	 * @param selectionResLbl the selection res lbl
	 */
	public void setSelectionResLbl(Label selectionResLbl) {
		this.selectionResLbl = selectionResLbl;
	}
}
