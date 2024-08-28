package com.paint.controller;


import com.paint.view.CanvasView;
import com.paint.view.UtilityMenu;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

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

		menuItemsFileSettings.get(1).setOnAction(this::handleFileSave);
		menuItemsFileSettings.get(2).setOnAction(this::handleFileSaveAs);
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
		try {
			Double canvasWidth = canvasView.getLayout().getWidth();
			Double canvasHeight = canvasView.getLayout().getHeight();

			Image image = new Image(selectedFile.toURI().toURL().toExternalForm(), canvasWidth, canvasHeight, true,true);
			canvasView.setImageViewImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleFileSave(ActionEvent event) {
//		try {
//
//		} catch (IOException e) {
//			System.out.println(e);
//		}
	}

	private void handleFileSaveAs(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG Files", "*.png")
				,new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
		);

		File file = fileChooser.showSaveDialog(null);
		WritableImage writableImage = this.canvasView.getWritableImage();
		this.canvasView.getCanvas().snapshot(null, writableImage);

		if (file != null) {
			saveImageToFile(writableImage, file);
		}

	}

	private void saveImageToFile(WritableImage writableImage, File file) {
//		try {
//			ImageIO.write(writableImage, "png", file);
//		} catch (IOException e) {
//			System.out.println(e);
//		}
	}
}
