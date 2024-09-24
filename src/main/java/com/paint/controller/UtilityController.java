package com.paint.controller;


import com.paint.model.CanvasModel;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.HelpAboutModel;
import com.paint.model.SettingStateModel;
import com.paint.resource.AutoSave;
import com.paint.resource.WebServerHandler;
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
	private final String DEFAULT_DIRNAME = ".paint/projects";
	private final String DEFAULT_ROOT_NAME = "user.home";

	@FXML
	public Button undoBtn;

	@FXML
	public Button redoBtn;

	private WorkspaceHandler workspaceHandler;

	@FXML
	public Button refreshIcon;

	@FXML
	public Label timerLabel;

	private HelpAboutModel helpAboutModel;
	private CanvasModel canvasModel;
	private AutoSaveController autoSaveController;
	private AutoSave autoSave;
	private WebServerHandler webServerHandler;

	public WebServerHandler getWebServerHandler() {
		return webServerHandler;
	}

	public void setWebServerHandler(WebServerHandler webServerHandler) {
		this.webServerHandler = webServerHandler;
	}

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

	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
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

			// Serve img
			webServerHandler.updateCurrentFile(selectedFile);
		} catch (IOException e) {
			new Alert(Alert.AlertType.ERROR, "Unable to create an image: ERROR: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void waitForImageLoad(Image image, File selectedFile) {
		// Wait for image to load
		image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
			if (newProgress.doubleValue() == 1.0) { // -> Image is loaded
				Workspace temp = this.workspaceHandler.getCurrentWorkspace();

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
		if (this.workspaceHandler.getCurrentWorkspace().getWorkspaceFile() == null) {
			// Redirect request to handleFileSaveAs
			handleFileSaveAs(event);
			return;
		}

		try {
			String filePath = this.workspaceHandler.getCurrentWorkspace().getWorkspaceFile().getAbsolutePath();
			String fileExt = getFileExt(filePath);
			File file = new File(filePath); // Find the previously saved file

			Workspace temp = this.workspaceHandler.getCurrentWorkspace();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					temp.getCanvasController().saveImageFromCanvas(file, fileExt);
				}
			});

			temp.setWorkspaceFile(file);

			// Serve new img on save
			webServerHandler.updateCurrentFile(file);

			// Reset timer on save
			seconds = (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60;
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
		Workspace currentWorkspace = this.workspaceHandler.getCurrentWorkspace();

		boolean allowSave = true;
		if (currentWorkspace.getWorkspaceFile() != null) { // File has been saved previously
			allowSave = handleSaveAsDiffExt(fileExt);
		}

		if (allowSave) {
			currentWorkspace.getCanvasController().saveImageFromCanvas(file, fileExt);
		Workspace temp = this.workspaceHandler.getCurrentWorkspace();
		temp.getCanvasController().saveImageFromCanvas(file, fileExt);

			currentWorkspace.setWorkspaceFile(file);
			// Serve new img on save
			webServerHandler.updateCurrentFile(file);
		}
	}

	private boolean handleSaveAsDiffExt(String newFileExt) {
		Workspace currentWorkspace = this.workspaceHandler.getCurrentWorkspace();
		String currentFileExt = getFileExt(currentWorkspace.getWorkspaceFile().getAbsolutePath());

		// Check if file is being saved with a different extension
		if (!newFileExt.equals(currentFileExt)) {
			// Show alert that data may be lost
			ButtonType bT = showDataLossAlert(currentFileExt, newFileExt);
			if (bT == ButtonType.OK) {
				return true;
			} else {
				return false;
			}
		} else {
			// Extensions are the same, allow user to save
			return true;
		}
	}

	private ButtonType showDataLossAlert(String currFile, String newFile) {
		Alert dataLossAlert = new Alert(Alert.AlertType.WARNING);
		dataLossAlert.setTitle("WARNING: POTENTIAL DATA LOSS");
		dataLossAlert.setContentText("" +
				"ALERT: You are about to save a new file with different extension than that of the original file." +
				"This action may lead to the irreversible loss of image data, going from extension: " + currFile +
				" to extension: " + newFile + " If you wish to continue press OK."
		);
		dataLossAlert.getButtonTypes().add(ButtonType.CANCEL);
		Optional<ButtonType> userResponse =  dataLossAlert.showAndWait();
		ButtonType buttonType = userResponse.orElse(ButtonType.CANCEL);
		dataLossAlert.showAndWait();

		return buttonType;
	}


	public String getFileExt(String filePath) {
		return filePath.substring(filePath.lastIndexOf(".") + 1); // Get string val after the last '.'
	}

	public File createFileChooserDir(String rootName, String dirName) {
		if (rootName == null) { // TODO adjust to catch empty strings
			rootName = DEFAULT_ROOT_NAME;
		}

		if (dirName == null) {
			dirName = DEFAULT_DIRNAME;
		}

		File paintFileDir = new File(System.getProperty(rootName), dirName);
		if (!paintFileDir.exists()) {
			paintFileDir.mkdirs();
		}

		return paintFileDir;
	}

	public String getDEFAULT_DIRNAME() { return DEFAULT_DIRNAME; }

	public String getDEFAULT_ROOT_NAME() { return DEFAULT_ROOT_NAME; }


	public void getLoadHelpDialog(ActionEvent actionEvent) throws IOException {
		this.helpAboutModel.loadHelpMenu();
	}

	public void getLoadAboutDialog(ActionEvent actionEvent) throws IOException {
		this.helpAboutModel.loadAboutMenu();
	}

	public void handleNewFile(ActionEvent actionEvent) throws IOException {
		Path tempFile = Files.createTempFile(Files.createTempDirectory("temp-dir"), "testData-", ".txt");

		File newFile = tempFile.toFile();
		this.workspaceHandler.setCurrentFile(newFile);
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
			autoSaveController.setSettingStateModel(this.workspaceHandler.getSettingStateModel());

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
		SettingStateModel stateModel = this.workspaceHandler.getSettingStateModel();
		// When apply is selected determine what vals have changed and apply
		stateModel.setAutosaveEnabled(autoSaveController.getAutoSaveEnabledCB().isSelected());
		stateModel.setTimerVisible(autoSaveController.getAutoSaveTimerVisibleCB().isSelected());
		stateModel.setAutoSaveInterval((long) autoSaveController.getAutoSaveIntervalSlider().getValue());

		hideAutoSaveTimer(autoSaveController.autoSaveTimerVisibleCB.isSelected());
	}

	private int seconds = 0;
	private Timeline timer;
	private int prevTimerLen = -1;

	public void hideAutoSaveTimer(boolean hide) {
		if (!hide) { // user wants timer hidden
			timerLabel.setText("Auto Save");
		} else { // user wants timer visible
			if (prevTimerLen < 0) { // First init
				prevTimerLen = (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60;
			}
			startAutoSaveTimer();
		}
	}

	private void startAutoSaveTimer() {
		handleRunningTimer();

		seconds = (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60;
		timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			seconds--;
			timerLabel.setText("" + seconds);
		}));

		timer.setCycleCount((int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60); // seconds -> minutes
		timer.play();
	}

	private void handleRunningTimer() {
		if(timer != null) {
			// Return early if timer is still running and the prev timer length is the same
			if (timer.getStatus() == Animation.Status.RUNNING && prevTimerLen == (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60) { // Use previous timer if val hasn't changed
				return;
			} else {
				timer.stop();
				prevTimerLen = (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60;
			}
		}
	}

	private void handleAutoSaveRefresh(MouseEvent mouseEvent) {
		// Show very cool & awesome animation
		// Reset timer thread
	}

	@FXML
	private void onCanvasClearMouseClick() {
		// Get current workspaces' canvasModel
		this.workspaceHandler.getCurrentWorkspace().getCanvasModel().clearCanvas();
	}

	@FXML
	private void onMouseClickedUndo() {
		this.workspaceHandler.getCurrentWorkspace().handleUndoAction();
	}

	@FXML
	private void onMouseClickedRedo() {
		this.workspaceHandler.getCurrentWorkspace().handleRedoAction();
	}

	public void onKeyPressedUndoBtn(KeyEvent keyEvent) {
		this.workspaceHandler.getCurrentWorkspace().handleUndoAction();
	}

	public void onKeyPressedRedoBtn(KeyEvent keyEvent) {
		this.workspaceHandler.getCurrentWorkspace().handleRedoAction();

	}
}
