package com.paint.controller;


import com.paint.view.CanvasView;
import com.paint.view.UtilityMenu;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;

public class UtilityController {
	private UtilityMenu utilityMenu;
	private CanvasView canvasView;

	public UtilityController (UtilityMenu utilityMenu, CanvasView canvasView) {

		this.utilityMenu = utilityMenu;
		this.canvasView = canvasView;
		attachEventHandlers();
	}

	private void attachEventHandlers() {
		// Retrieve all menus from the Menu Bar
		ObservableList<Menu> menuItemObservableList = utilityMenu.getMenuBar().getMenus();

		// Retrieve each MenuItem from the File Settings menu
		ObservableList<MenuItem> menuItemsFileSettings = menuItemObservableList.get(0).getItems();

		// Index 0 is the first Menu which contains File settings
		menuItemsFileSettings.get(0).setOnAction(this::handleFileOpen); // TODO convert this into a loop to add event listeners to each menuItem

	}

	private void handleFileOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select Image");

		// Dictates which file types are allowed
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG Files", "*.png")
				,new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
		);

		File selectedFile = fileChooser.showOpenDialog(null);

		// Create new Image object and update the canvas imageView
		Image image = new Image(selectedFile.getAbsolutePath());
		canvasView.setImageViewImage(image);

	}
}
