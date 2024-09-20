package com.paint.controller;

import com.paint.model.CanvasModel;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.InfoCanvasModel;
import com.paint.model.PaintStateModel;
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
    public Label lineWidthLbl;

    @FXML
	private Label resolutionLbl;

	@FXML
	private Label selectionResLbl;

	@FXML
	private Label mousePosLbl;

	private CanvasModel canvasModel;

	private Group canvasGroup;

	private double currentZoomLvl;

	private InfoCanvasModel infoCanvasModel;

	private PaintStateModel paintStateModel;

	private WorkspaceHandler workspaceHandler;

	public WorkspaceHandler getCurrentWorkspaceModel() {
		return workspaceHandler;
	}

	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	public ComboBox<String> getInfoBarZoomCB() {
		return infoBarZoomCB;
	}

	public void setInfoBarZoomCB(ComboBox<String> infoBarZoomCB) {
		this.infoBarZoomCB = infoBarZoomCB;
	}

	public PaintStateModel getPaintStateModel() {
		return paintStateModel;
	}

	public void setPaintStateModel(PaintStateModel paintStateModel) {
		this.paintStateModel = paintStateModel;
	}

	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;

		// Bind text props for resolution, mouse POS, & selection resolution
		this.mousePosLbl.textProperty().bind(this.infoCanvasModel.getMousePosLbl().textProperty());
		this.resolutionLbl.textProperty().bind(this.infoCanvasModel.getResolutionLbl().textProperty());
		this.selectionResLbl.textProperty().bind(this.infoCanvasModel.getSelectionResLbl().textProperty());
		this.lineWidthLbl.textProperty().bind(this.infoCanvasModel.getCurrentLineWidthLbl().textProperty());
	}

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
		this.canvasGroup = canvasModel.getCanvasGroup();
	}

	@FXML
	private void onZoomCBChange(ActionEvent actionEvent) {
		// Set zoom slider to CB zoom value

		// Convert string -> double
		try {
			String cbVal = this.infoBarZoomCB.getValue();

			// Check if there is a '%' symbol
			// TODO implement more thorough validation since this test passes if there is simply a '%' anywhere
			if (cbVal.lastIndexOf("%") != -1) { // Eval -> true means that there is a '%' symbol
				cbVal = cbVal.substring(0, cbVal.length() - 1); // Remove '%' symbol
			}

			double sliderVal = Double.parseDouble(cbVal); // Convert string -> double to set slider POS
			this.infoBarZoomSlider.setValue(sliderVal);

			this.workspaceHandler.getCurrentWorkspace().getCanvasModel().setZoomScale(sliderVal / 100.0);

		} catch (Exception e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}

		handleZoom();
	}

	@FXML
	private void onZoomSliderChange(MouseEvent mouseEvent) {
		// Set zoom combo box to zoom value
		this.infoBarZoomCB.setValue(String.valueOf(this.infoBarZoomSlider.getValue()));
		this.workspaceHandler.getCurrentWorkspace().getCanvasModel().setZoomScale(infoBarZoomSlider.getValue() / 100.0);
		handleZoom();
	}

	// Handle scaling/zoom
	private void handleZoom() {
		canvasGroup = this.workspaceHandler.getCurrentWorkspace().getCanvasModel().getCanvasGroup();
		double newZoomScale = this.workspaceHandler.getCurrentWorkspace().getCanvasModel().getZoomScale(); // Divide by 100.0 to get proper format for setScaleX/Y
		canvasGroup.setScaleX(newZoomScale);
		canvasGroup.setScaleY(newZoomScale);
	}

}
