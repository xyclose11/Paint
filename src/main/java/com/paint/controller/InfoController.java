package com.paint.controller;

import com.paint.model.CanvasModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class InfoController {
	@FXML
	public ComboBox<String> infoBarZoomCB;

	@FXML
	public Slider infoBarZoomSlider;

	@FXML
	private Label resolutionLbl;

	@FXML
	private Label selectionResLbl;

	@FXML
	private Label mousePosLbl;

	private CanvasModel canvasModel;

	private Group canvasGroup;

	private double currentZoomLvl;

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
		this.canvasGroup = canvasModel.getCanvasGroup();
	}

	@FXML
	private void onCanvasSizeChange() {

	}

	@FXML
	private void onZoomCBChange(ActionEvent actionEvent) {
		// Set zoom slider to CB zoom value

		// Convert string -> double
		try {
			String cbVal = this.infoBarZoomCB.getValue();
			cbVal = cbVal.substring(0, cbVal.length() - 1); // Remove '%' symbol
			double sliderVal = Double.parseDouble(cbVal);

			this.infoBarZoomSlider.setValue(sliderVal);

			currentZoomLvl = sliderVal;

		} catch (Exception e) {
			e.printStackTrace();
		}

		handleZoom();
	}

	@FXML
	private void onZoomSliderChange(MouseEvent mouseEvent) {
		// Set zoom combo box to zoom value
		this.infoBarZoomCB.setValue(String.valueOf(infoBarZoomSlider.getValue()));
		currentZoomLvl = infoBarZoomSlider.getValue();
		handleZoom();
	}

	private void handleZoom() {
		canvasGroup = canvasModel.getCanvasGroup();
		double newZoomScale = currentZoomLvl / 100.0; // Divide by 100.0 to get proper format for setScaleX/Y

		canvasGroup.setScaleX(newZoomScale);
		canvasGroup.setScaleY(newZoomScale);

	}

	@FXML
	private void initialize(){

	}




}
