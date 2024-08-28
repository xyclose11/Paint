package com.paint.controller;


import com.paint.view.CanvasView;
import com.paint.view.UtilityMenu;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Date;

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

		if (selectedFile == null) { // TODO make this alert the user that the file had an error with importing
			return;
		}

		// Create new Image object and update the canvas imageView
		try {
			Double canvasWidth = canvasView.getLayout().getWidth();
			Double canvasHeight = canvasView.getLayout().getHeight();

			Image image = new Image(selectedFile.toURI().toURL().toExternalForm(),true);
			canvasView.getLayout().setMinSize(image.getWidth(), image.getHeight());

			canvasView.setImageViewImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleFileSave(ActionEvent event) {
		Image imageToSave = canvasView.getImageViewImage();

		if (imageToSave == null) {
			return; // TODO remove this when the canvas is implemented and you can save WritableImages
		}

		// Remove "file:" from the image's path
		String splitFileName = imageToSave.getUrl().substring(6); // TODO find a better way to implement this
		File file = new File(splitFileName);

		// Sets the last updated date/time for the image/file
		file.setLastModified(new Date().getTime());

		String fileExtension = file.getName().substring(file.getName().lastIndexOf("."));
		saveImageToFile(imageToSave, file, fileExtension);
	}

	private void handleFileSaveAs(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG Files", "*.png")
				,new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
		);

		File paintFileDir = new File(System.getProperty("user.home"), ".paint/projects");
		if (!paintFileDir.exists()) {
			paintFileDir.mkdirs();
		}

		fileChooser.setTitle("Save Image");

		fileChooser.setInitialFileName("image");

		fileChooser.setInitialDirectory(paintFileDir);

		File file = fileChooser.showSaveDialog(null);

//		WritableImage writableImage = this.canvasView.getWritableImage(); // Code may be useful in the future when the image needs to be editable
//		writableImage = this.canvasView.getImageView().snapshot(null, writableImage);

		Image imageToSave = this.canvasView.getImageViewImage(); // Temporary code to only display the image itself

		// Get file extension
		String fileExtension = file.getName().substring(file.getName().lastIndexOf("."));
		if (file != null) {
			saveImageToFile(imageToSave, file, fileExtension);
		}

	}

	private void saveImageToFile(Image image, File file, String fileExtension) {
		try {
			// Create new file or overwrite file with same name, with designated fileExtension at the path file
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), fileExtension, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
