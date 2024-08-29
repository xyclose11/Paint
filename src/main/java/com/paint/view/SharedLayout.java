package com.paint.view;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SharedLayout {
	private BorderPane layout;
	private UtilityMenu utilityMenu;
	private ToolMenu toolMenu;
	private CanvasView canvasView;
	private InfoBar infoBar;

	public SharedLayout() {
		layout = new BorderPane();
		utilityMenu = new UtilityMenu();
		toolMenu = new ToolMenu();
		canvasView = new CanvasView();
		infoBar = new InfoBar();

		// Create a VBox wrapper for both utilityMenu & toolMenu
		VBox topWrapper = new VBox();
		topWrapper.getChildren().addAll(utilityMenu.getLayout(), toolMenu.getLayout());

		// Setup initial layout
		layout.setTop(topWrapper);
		layout.setCenter(canvasView.getLayout());
		layout.setBottom(infoBar.getLayout());
	}

	public BorderPane getLayout() {
		return layout;
	}

	public CanvasView getCanvasView() {
		return canvasView;
	}
	public UtilityMenu getUtilityMenu() {
		return utilityMenu;
	}
}
