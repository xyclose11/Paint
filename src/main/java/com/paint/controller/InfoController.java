package com.paint.controller;

import com.paint.model.CanvasModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InfoController {
	@FXML
	private Label resolutionLbl;

	private CanvasModel canvasModel;

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;

		bindCanvasResToLbl();
	}

	// Binds the canvas resolution to the label so that it updates when the img size changes
	private void bindCanvasResToLbl() {
		if (canvasModel != null) {
			resolutionLbl.textProperty().bind(
					canvasModel.canvasWidthProperty().asString()
							.concat(" x ")
							.concat(canvasModel.canvasHeightProperty().asString())
			);
		}
	}

}
