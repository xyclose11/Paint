package com.paint.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ToolMenu {
	private HBox layout;
	private Label tempLabel; // WILL BE REMOVED
	public ToolMenu() {
		layout = new HBox();
		layout.setMinHeight(60);
		layout.setPrefHeight(80);
		tempLabel = new Label();
		tempLabel.setText("This section is work in progress");
		layout.getChildren().add(tempLabel);
	}

	public void setLayout(HBox layout) {
		this.layout = layout;
	}

	public HBox getLayout() {
		return layout;
	}

	public Label getTempLabel() {
		return tempLabel;
	}

	public void setTempLabel(Label tempLabel) {
		this.tempLabel = tempLabel;
	}
}
