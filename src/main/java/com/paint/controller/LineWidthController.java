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

	@FXML
	private void initialize() {
		// Default slider settings
		this.lineWidthSlider.setMajorTickUnit(0.25f);
		this.lineWidthSlider.setMinorTickCount(1);
		this.lineWidthSlider.setMax(80);
		this.lineWidthSlider.setValue(0.5f);
		this.lineWidthSlider.setMin(0);
		this.lineWidthSlider.setShowTickLabels(true); // TODO create popup in canvas to show size of line

	}
}
