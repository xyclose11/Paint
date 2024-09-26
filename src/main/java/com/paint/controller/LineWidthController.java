package com.paint.controller;

import com.paint.model.PaintStateModel;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

/**
 * This class controls the Line Width view when a user has either the 'brush' or 'eraser' tool selected
 *
 * NOTE: This class loads a new instance of the same FXML file on each load
 * @since 1.1
 * */
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

	@FXML // TODO save the state of the FXML so it doesn't reset the Val here
	private void initialize() {
		// Default slider settings
		this.lineWidthSlider.setMajorTickUnit(8.0f);
		this.lineWidthSlider.setMinorTickCount(1);
		this.lineWidthSlider.setMax(120);
		this.lineWidthSlider.setValue(12.0f); // Default LW
		this.lineWidthSlider.setMin(1);
		this.lineWidthSlider.setShowTickLabels(true); // TODO create popup in canvas to show size of line

	}
}
