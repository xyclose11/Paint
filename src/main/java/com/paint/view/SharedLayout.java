package com.paint.view;

import javafx.scene.layout.VBox;

public class SharedLayout {
	private VBox layout;
	private UtilityMenu utilityMenu;
	private ToolMenu toolMenu;
	private CanvasView canvasView;
	private InfoBar infoBar;

	public SharedLayout() {
		layout = new VBox();
		utilityMenu = new UtilityMenu();
		toolMenu = new ToolMenu();
		canvasView = new CanvasView();
		infoBar = new InfoBar();
		layout.getChildren().addAll(utilityMenu.getLayout(), toolMenu.getLayout(), canvasView.getLayout(), infoBar.getLayout());
	}

	public VBox getLayout() {
		return layout;
	}

	public CanvasView getCanvasView() {
		return canvasView;
	}
	public UtilityMenu getUtilityMenu() {
		return utilityMenu;
	}
}
