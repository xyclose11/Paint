package com.paint.controller;

import com.paint.model.PaintStateModel;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class LineWidthController {
	private PaintStateModel paintStateModel;

	@FXML
	private Slider lineWidthSlider;

	public void setPaintStateModel(PaintStateModel paintStateModel) {
		this.paintStateModel = paintStateModel;
	}

	@FXML
	private void onSliderValueChanged(MouseEvent mouseEvent) {
		// Change paintStateModel.lineWidth
		this.paintStateModel.setCurrentLineWidth(lineWidthSlider.getValue());
	}
}
