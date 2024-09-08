package com.paint.controller;


import com.paint.model.CanvasModel;
import com.paint.model.HelpAboutModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UtilityController {
	private File currentFile; // Used to manage state during runtime

	private CanvasController canvasController;
	private CanvasModel canvasModel;

	private HelpAboutModel helpAboutModel;

	public void setHelpAboutModel(HelpAboutModel helpAboutModel) {
		this.helpAboutModel = helpAboutModel;
	}

	// Setup communication between controllers
	public void setCanvasController(CanvasController canvasController) {
		this.canvasController = canvasController;
	}

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	public UtilityController() {
		currentFile = null;
	}

	@FXML
	private void handleFileOpen(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Select Image");

		// Dictates which file types are allowed
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png")
				,new FileChooser.ExtensionFilter("JPEG (*.jpeg, *.jpg, *jpe)", "*.jpeg", "*.jpg", "*.jpe")
//				,new FileChooser.ExtensionFilter("Monochrome Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib") // TODO Add variations to bitmap quality
//				,new FileChooser.ExtensionFilter("16 Color Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
//				,new FileChooser.ExtensionFilter("256 Color Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
//				,new FileChooser.ExtensionFilter("24-Bit Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
				,new FileChooser.ExtensionFilter("Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
		);

		File paintFileDir = createFileChooserDir(null, null);
		fileChooser.setInitialDirectory(paintFileDir);

		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile == null) { // TODO create a corrupt image file to test
			// Show an alert if the desired file had issues uploading
			new Alert(Alert.AlertType.INFORMATION, "No File Selected");
			return;
		}

		// Adjust state of currentFile
		currentFile = selectedFile;

		canvasModel.setCurrentFile(currentFile);

		try  {
			// Create base Image
			Image image = new Image(selectedFile.toURI().toURL().toExternalForm(),true);
			waitForImageLoad(image);
		} catch (IOException e) {
			new Alert(Alert.AlertType.ERROR, "Unable to create an image: ERROR: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void waitForImageLoad(Image image) {
		// Wait for image to load
		image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
			if (newProgress.doubleValue() == 1.0) { // -> Image is loaded
				canvasController.setCanvas(image);
			}
		});

	}

	// Save image process
	// 1. Create an empty WritableImage object with desired dimensions
	// 2. Take a canvas snapshot (This will capture every pixel on the canvas)
	// 3. Create a BufferedImage object using SwingFXUtils.fromFXImage(writableImage, null)
	// 4. Create a File object that contains the desired save path
	// 5. Call ImageIO.write()

	@FXML
	public void handleFileSave(ActionEvent event) throws IOException {
		// Check if there is a current file opened
		if (currentFile == null) {
			// Redirect request to handleFileSaveAs
			handleFileSaveAs(event);
			return;
		}

		String filePath = currentFile.getAbsolutePath();
		String fileExt = getFileExt(filePath);
		File file = new File(filePath); // Find the previously saved file

		canvasController.saveImageFromCanvas(file, fileExt);

		canvasModel.setCurrentFile(file);

	}

	@FXML
	private void handleFileSaveAs(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();

		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png")
				,new FileChooser.ExtensionFilter("JPEG (*.jpg)", "*.jpg") // TODO Add support for each variation of JPEG, JPG, JPE, etc
//				,new FileChooser.ExtensionFilter("Monochrome Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib") // TODO Add variations to bitmap quality Source (https://atlc.sourceforge.net/bmp.html)
//				,new FileChooser.ExtensionFilter("16 Color Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
//				,new FileChooser.ExtensionFilter("256 Color Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
//				,new FileChooser.ExtensionFilter("24-Bit Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
				,new FileChooser.ExtensionFilter("Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
		);

		File paintFileDir = createFileChooserDir(null, null);
		fileChooser.setInitialDirectory(paintFileDir);
		fileChooser.setTitle("Save Image");
		fileChooser.setInitialFileName("Untitled"); // Default filename // TODO use filenameFilter to check if the name already exists

		File file = fileChooser.showSaveDialog(null);

		// Error handling
		if (file == null) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION, "No File Selected");
			alert.show();
			return;
		}
		// Update currentFile state
		currentFile = file;

		String fileExt = getFileExt(file.getAbsolutePath());

		canvasController.saveImageFromCanvas(file, fileExt);

		// Set currentOpenFile
		canvasModel.setCurrentFile(file);
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


	public void getLoadHelpDialog(ActionEvent actionEvent) throws IOException {
		this.helpAboutModel.loadHelpMenu();
	}

	public void getLoadAboutDialog(ActionEvent actionEvent) throws IOException {
		this.helpAboutModel.loadAboutMenu();
	}
}
