package com.paint.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class InfoBar {
	private HBox layout;
	private Label label;

	public InfoBar() {
		layout = new HBox();
		layout.setMinHeight(7.4);
		layout.setMaxHeight(12);
		layout.setPrefHeight(10);
		label = new Label();
		label.setText("This is the info bar");
		layout.getChildren().addAll(label);
	}

	public HBox getLayout() {
		return layout;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public void setLayout(HBox layout) {
		this.layout = layout;
	}
}
