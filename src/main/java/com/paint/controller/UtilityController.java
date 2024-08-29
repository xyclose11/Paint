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

		File paintFileDir = createFileChooserDir(null, null);

		fileChooser.setInitialDirectory(paintFileDir);


		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile == null) { // TODO make this alert the user that the file had an error with importing
			return;
		}

		// Create new Image object and update the canvas imageView
		try {

			// Create base Image
			Image image = new Image(selectedFile.toURI().toURL().toExternalForm(),true);

			// Wait for image to load
			image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
				if (newProgress.doubleValue() == 1.0) { // Image is loaded
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

	private void handleFileSave(ActionEvent event) {
		Image imageToSave = canvasView.getImageViewImage();

		// If there is no image to save create a blank image & save as
		// TODO once canvas is setup to integrate images convert this to handle appropriate files
		if (imageToSave == null || imageToSave.getUrl() == null) {
			handleFileSaveAs(event);
			return;
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
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png")
				,new FileChooser.ExtensionFilter("JPEG (*.jpeg)", "*.jpeg")
		);

		File paintFileDir = createFileChooserDir(null, null);

		fileChooser.setInitialDirectory(paintFileDir);

		fileChooser.setTitle("Save Image");

		fileChooser.setInitialFileName("image"); // TODO add incremental count if the filename already exists

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
