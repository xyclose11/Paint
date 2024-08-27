package com.paint.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ToolMenu {
	private HBox rootContainer;
	private Label tempLabel; // WILL BE REMOVED
	public ToolMenu() {
		rootContainer = new HBox();
		tempLabel = new Label();
	}

	public HBox Init() {
		tempLabel.setText("This section is work in progress");
		rootContainer.getChildren().add(tempLabel);
		return rootContainer;
	}
}
