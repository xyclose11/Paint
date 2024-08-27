package com.paint.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InfoBar {
	private HBox container;
	private Label label;

	public InfoBar() {
		container = new HBox();
		label = new Label();
	}

	public HBox Init() {
		label.setText("This is the info bar");
		container.getChildren().addAll(label);
		return container;
	}
}
