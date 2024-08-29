package com.paint.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InfoBar {
	private HBox layout;
	private Label mousePosLbl;
	private Label selectionPosLbl;
	private Label resLabel;


	public InfoBar() {
		layout = new HBox();
		layout.setMinHeight(14);
		layout.setMaxHeight(20);
		HBox leftContainer = new HBox();
		HBox rightContainer = new HBox();
		mousePosLbl = new Label("Mouse Pos");
		selectionPosLbl = new Label("Selection Pos");
		resLabel = new Label("Resolution"); // TODO create infoBar controller and dynamically update resolution

		leftContainer.getChildren().addAll(mousePosLbl, selectionPosLbl, resLabel);
		leftContainer.setSpacing(20);
		// Redo these
		Label fitToScreen = new Label("Fit To Screen");
		Label zoomComboBox = new Label("Zoom ComboBox");
		Label zoomSlider = new Label("Zoom Slider");

		rightContainer.getChildren().addAll(fitToScreen, zoomComboBox, zoomSlider);
		rightContainer.setSpacing(20);

		layout.getChildren().addAll(leftContainer, rightContainer);
		layout.setSpacing(200); // TODO Get the percentage value of the total width of the window
		layout.setPadding(new Insets(0, 20, 0, 20));
	}

	public HBox getLayout() {
		return layout;
	}


	public void setLayout(HBox layout) {
		this.layout = layout;
	}
}
