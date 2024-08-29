package com.paint.controller;


import com.paint.view.CanvasView;
import com.paint.view.UtilityMenu;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UtilityController {
	private UtilityMenu utilityMenu;
	private CanvasView canvasView;
	private File currentFile; // Used to manage state during runtime

	public UtilityController (UtilityMenu utilityMenu, CanvasView canvasView) {

		this.utilityMenu = utilityMenu;
		this.canvasView = canvasView;
		currentFile = null;   // Initialized as null currently since each app startup will be blank | TODO alter state management
		attachEventHandlers();// when you are able to reconstruct a previous project on initialization
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

		File paintFileDir = createFileChooserDir(null, null);
		fileChooser.setInitialDirectory(paintFileDir);

		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile == null) { // TODO make this alert the user that the file had an error with importing
			return;
		}

		// Adjust state of currentFile
		currentFile = selectedFile;

		// Create new Image object and update the canvas imageView
		try {

			// Create base Image
			Image image = new Image(selectedFile.toURI().toURL().toExternalForm(),true);

			// Wait for image to load
			image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
				if (newProgress.doubleValue() == 1.0) { // SUCCESS -> Image is loaded
					// Get pixelReader to convert Image to a WritableImage to set the main canvas
					PixelReader pixelReader = image.getPixelReader();
					WritableImage writableImage = new WritableImage(pixelReader, (int) (image.getWidth()), (int)(image.getHeight()));
					canvasView.getCanvas().getGraphicsContext2D().drawImage(writableImage, 0, 0);

					// Set canvas dimensions to match image dimensions
					canvasView.getCanvas().setWidth(image.getWidth());
					canvasView.getCanvas().setHeight(image.getHeight());
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// Save image process
	// 1. Create an empty WritableImage object with desired dimensions
	// 2. Take a canvas snapshot (This will capture every pixel on the canvas)
	// 3. Create a BufferedImage object using SwingFXUtils.fromFXImage(writableImage, null)
	// 4. Create a File object that contains the desired save path
	// 5. Call ImageIO.write()

	private void handleFileSave(ActionEvent event) {
		// Check if there is a current file opened
		if (currentFile == null) {
			// Redirect request to handleFileSaveAs
			handleFileSaveAs(event);
			return;
		}

		String filePath = currentFile.getAbsolutePath();
		String fileExt = getFileExt(filePath);
		File file = new File(filePath); // Find the previously saved file

		saveImageToFile(file, fileExt);
	}

	private void handleFileSaveAs(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png")
				,new FileChooser.ExtensionFilter("JPEG (*.jpeg)", "*.jpeg")
		);

		File paintFileDir = createFileChooserDir(null, null);
		fileChooser.setInitialDirectory(paintFileDir);
		fileChooser.setTitle("Save Image");
		fileChooser.setInitialFileName("Untitled"); // Default filename // TODO use filenameFilter to check if the name already exists


		File file = fileChooser.showSaveDialog(null);
		String fileExt = getFileExt(file.getAbsolutePath());

		if (file != null) {
			saveImageToFile(file, fileExt);
		}

	}

	// Takes a snapshot of the canvas & saves it to the designated file
	private void saveImageToFile(File file, String fileExtension) {
		WritableImage writableImage = new WritableImage((int)(canvasView.getCanvas().getWidth()), (int) (canvasView.getCanvas().getHeight()));
		this.canvasView.getCanvas().snapshot(null, writableImage);

		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
		try {
			// Create new file or overwrite file with same name, with designated fileExtension at the path file
			ImageIO.write(bufferedImage, fileExtension, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getFileExt(String filePath) {
		return filePath.substring(filePath.lastIndexOf(".") + 1); // Get string val after the last '.'
	}

	private File createFileChooserDir(String rootName, String dirName) {
		if (rootName == null) { // TODO adjust to catch empty strings
			rootName = "user.home";
		}

		if (dirName == null) {
			dirName = ".paint/projects";
		}

		File paintFileDir = new File(System.getProperty(rootName), dirName);
		if (!paintFileDir.exists()) {
			paintFileDir.mkdirs();
		}

		return paintFileDir;
	}
}
