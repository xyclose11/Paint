package com.paint.model;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class InfoCanvasModel {

	private Label resolutionLbl = new Label("");

	private Label selectionResLbl = new Label("");

	private Label mousePosLbl = new Label("");

	private Label currentLineWidthLbl = new Label("1px");

	public Label getCurrentLineWidthLbl() {
		return currentLineWidthLbl;
	}

	public void setCurrentLineWidthLbl(double lineWidth) {
		this.currentLineWidthLbl.setText((int)lineWidth + "px");
	}

	// !!! Returning the Label object for binding purposes !!!
	public Label getMousePosLbl() {
		return mousePosLbl;
	}

	public void setMousePosLbl(MouseEvent mouseEvent) {
		mousePosLbl.setText("X: " + Math.round(mouseEvent.getX()) + " Y: " + Math.round(mouseEvent.getY()));
	}

	public Label getResolutionLbl() {
		return resolutionLbl;
	}

	public void setResolutionLblText(double x, double y) {
		this.resolutionLbl.setText(x + " x " + y + "px");
	}

	public Label getSelectionResLbl() {
		return selectionResLbl;
	}

	public void setSelectionResLbl(Label selectionResLbl) {
		this.selectionResLbl = selectionResLbl;
	}
}
