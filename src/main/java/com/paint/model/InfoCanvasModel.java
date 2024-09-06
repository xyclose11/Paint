package com.paint.model;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class InfoCanvasModel {

	private Label resolutionLbl;

	private Label selectionResLbl;

	private Label mousePosLbl = new Label("");

	public Label getMousePosLbl() {
		return mousePosLbl;
	}

	public void setMousePosLbl(MouseEvent mouseEvent) {
		System.out.println(mouseEvent.getX());
		System.out.println(mouseEvent.getY());
		mousePosLbl.setText("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY());
	}
}
