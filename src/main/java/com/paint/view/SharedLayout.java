package com.paint.view;

import javafx.scene.layout.VBox;

public class SharedLayout {
	private VBox rootContainer;
	private UtilityMenu utilityMenu;
	private ToolMenu toolMenu;
	private CanvasView canvasView;
	private InfoBar infoBar;

	public SharedLayout() {
		rootContainer = new VBox();
		utilityMenu = new UtilityMenu();
		toolMenu = new ToolMenu();
		canvasView = new CanvasView();
		infoBar = new InfoBar();
	}

	public VBox Init() {
		rootContainer.getChildren().addAll(utilityMenu.Init(), toolMenu.Init(), canvasView.Init(), infoBar.Init());
		return rootContainer;
	}
}
