package com.paint.controller;

import com.paint.model.CanvasModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InfoController {
	@FXML
	private Label resolutionLbl;

	@FXML
	private Label selectionResLbl;

	@FXML
	private Label mousePosLbl;

	private CanvasModel canvasModel;

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	@FXML
	private void onCanvasDimChange() {

	}

	@FXML
	private void initialize(){

	}


}
