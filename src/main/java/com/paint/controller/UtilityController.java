package com.paint.controller;


import com.paint.model.CanvasModel;
import com.paint.model.CurrentWorkspaceModel;
import com.paint.model.HelpAboutModel;
import com.paint.model.SettingStateModel;
import com.paint.resource.AutoSave;
import com.paint.resource.Workspace;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class UtilityController {

	@FXML
	public Button undoBtn;

	@FXML
	public Button redoBtn;

	@FXML
	public Button refreshIcon;

	@FXML
	public Label timerLabel;

	private CurrentWorkspaceModel currentWorkspaceModel;
	private HelpAboutModel helpAboutModel;
	private CanvasModel canvasModel;
	private AutoSaveController autoSaveController;
	private AutoSave autoSave;

	public AutoSaveController getAutoSaveController() {
		return autoSaveController;
	}

	public void setAutoSaveController(AutoSaveController autoSaveController) {
		this.autoSaveController = autoSaveController;
	}

	public AutoSave getAutoSave() {
		return autoSave;
	}

	public void setAutoSave(AutoSave autoSave) {
		this.autoSave = autoSave;
	}

	public CanvasModel getCanvasModel() {
		return canvasModel;
	}

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	public void setHelpAboutModel(HelpAboutModel helpAboutModel) {
		this.helpAboutModel = helpAboutModel;
	}

	public void setCurrentWorkspaceModel(CurrentWorkspaceModel currentWorkspaceModel) {
		this.currentWorkspaceModel = currentWorkspaceModel;
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

		try  {
			// Create base Image
			Image image = new Image(selectedFile.toURI().toURL().toExternalForm(),true);
			waitForImageLoad(image, selectedFile);
		} catch (IOException e) {
			new Alert(Alert.AlertType.ERROR, "Unable to create an image: ERROR: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void waitForImageLoad(Image image, File selectedFile) {
		// Wait for image to load
		image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
			if (newProgress.doubleValue() == 1.0) { // -> Image is loaded
				Workspace temp = this.currentWorkspaceModel.getCurrentWorkspace();

				// Apply image to the current open workspace
				temp.getCanvasController().setCanvas(image);
				temp.getCanvasModel().setChangesMade(true);
				temp.setWorkspaceFile(selectedFile);
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
		if (this.currentWorkspaceModel.getCurrentWorkspace().getWorkspaceFile() == null) {
			// Redirect request to handleFileSaveAs
			handleFileSaveAs(event);
			return;
		}

		try {
			String filePath = this.currentWorkspaceModel.getCurrentWorkspace().getWorkspaceFile().getAbsolutePath();
			String fileExt = getFileExt(filePath);
			File file = new File(filePath); // Find the previously saved file

			Workspace temp = this.currentWorkspaceModel.getCurrentWorkspace();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					temp.getCanvasController().saveImageFromCanvas(file, fileExt);
				}
			});

			temp.setWorkspaceFile(file);

			// Reset timer on save
			seconds = (int) autoSaveController.getSettingStateModel().getAutoSaveInterval() * 60;
			autoSave.restartAutoSaveService();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		String fileExt = getFileExt(file.getAbsolutePath());

		Workspace temp = this.currentWorkspaceModel.getCurrentWorkspace();
		temp.getCanvasController().saveImageFromCanvas(file, fileExt);

		temp.setWorkspaceFile(file);
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

	public void handleNewFile(ActionEvent actionEvent) throws IOException {
		Path tempFile = Files.createTempFile(Files.createTempDirectory("temp-dir"), "testData-", ".txt");

		File newFile = tempFile.toFile();
		this.currentWorkspaceModel.setCurrentFile(newFile);
	}

	@FXML
	public void initialize() {
		// Setup event handlers for autosave

		// Refresh icon clicked -> reset timer to current val from settings
		refreshIcon.setOnMouseClicked(this::handleAutoSaveRefresh);

		// Hide timer
		timerLabel.setOnMouseClicked(this::handleTimerLabelVisibility);
	}

	private void handleTimerLabelVisibility(MouseEvent mouseEvent) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AutoSaveSetting.fxml"));
			DialogPane dialogPane = fxmlLoader.load();

			// Load autoSaveController from view
			autoSaveController = fxmlLoader.getController();
			autoSaveController.setSettingStateModel(this.currentWorkspaceModel.getSettingStateModel());

			Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
			aboutAlert.setTitle("AutoSave Settings");
			aboutAlert.setDialogPane(dialogPane);
			Optional<ButtonType> result = aboutAlert.showAndWait();
			ButtonType buttonType = result.orElse(ButtonType.CANCEL);
			handleAutoSaveDialogResult(buttonType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleAutoSaveDialogResult(ButtonType result) {
		if (result != ButtonType.APPLY) { // don't apply settings
			return;
		}

		// Apply settings
		SettingStateModel stateModel = this.currentWorkspaceModel.getSettingStateModel();
		// When apply is selected determine what vals have changed and apply
		stateModel.setAutosaveEnabled(autoSaveController.getAutoSaveEnabledCB().isSelected());
		stateModel.setTimerVisible(autoSaveController.getAutoSaveTimerVisibleCB().isSelected());
		stateModel.setAutoSaveInterval((long) autoSaveController.getAutoSaveIntervalSlider().getValue());

		hideAutoSaveTimer(autoSaveController.autoSaveTimerVisibleCB.isSelected());

	}

	private int seconds = 0;
	private Timeline timer;
	private int prevTimerLen = -1;

	private void hideAutoSaveTimer(boolean hide) {
		if (!hide) { // user wants timer hidden
			timerLabel.setText("Auto Save");
		} else { // user wants timer visible
			if (prevTimerLen < 0) { // First init
				prevTimerLen = (int) autoSaveController.getSettingStateModel().getAutoSaveInterval() * 60;
			}

			if(timer != null) {
				if (timer.getStatus() == Animation.Status.RUNNING && prevTimerLen == (int) autoSaveController.getSettingStateModel().getAutoSaveInterval() * 60) { // Use previous timer if val hasn't changed
					return;
				} else {
					timer.stop();
					prevTimerLen = (int) autoSaveController.getSettingStateModel().getAutoSaveInterval() * 60;
				}
			}

			seconds = (int) autoSaveController.getSettingStateModel().getAutoSaveInterval() * 60;
			timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
				seconds--;
				timerLabel.setText("" + seconds);
			}));

			timer.setCycleCount((int) autoSaveController.getSettingStateModel().getAutoSaveInterval() * 60); // seconds -> minutes

			timer.play();
		}
	}

	private void handleAutoSaveRefresh(MouseEvent mouseEvent) {
		// Show very cool & awesome animation
		// Reset timer thread
	}

	@FXML
	private void onCanvasClearMouseClick() {
		// Get current workspaces' canvasModel
		this.currentWorkspaceModel.getCurrentWorkspace().getCanvasModel().clearCanvas();
	}

	@FXML
	private void onMouseClickedUndo() {
		this.currentWorkspaceModel.getCurrentWorkspace().handleUndoAction();
	}

	@FXML
	private void onMouseClickedRedo() {
		this.currentWorkspaceModel.getCurrentWorkspace().handleRedoAction();
	}

	public void onKeyPressedUndoBtn(KeyEvent keyEvent) {
		this.currentWorkspaceModel.getCurrentWorkspace().handleUndoAction();
	}

	public void onKeyPressedRedoBtn(KeyEvent keyEvent) {
		this.currentWorkspaceModel.getCurrentWorkspace().handleRedoAction();

	}
}
