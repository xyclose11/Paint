package com.paint.controller;


import com.paint.handler.NotificationsHandler;
import com.paint.handler.WebServerHandler;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.CanvasModel;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * This class handles all File related operations (Save, Save-As, Open, etc.)
 *
 * @since 1.0
 * */
public class UtilityController {
	private final String DEFAULT_DIRNAME = ".paint/projects";
	private final String DEFAULT_ROOT_NAME = "user.home";
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The Undo btn.
	 */
	@FXML
	public Button undoBtn;

	/**
	 * The Redo btn.
	 */
	@FXML
	public Button redoBtn;


	/**
	 * The Timer label.
	 */
	@FXML
	public Label timerLabel;

	private HelpAboutModel helpAboutModel;
	private CanvasModel canvasModel;
	private AutoSaveController autoSaveController;
	private AutoSave autoSave;
	private WorkspaceHandler workspaceHandler;
	private WebServerHandler webServerHandler;
	private NotificationsHandler notificationsHandler;

	@FXML
	private void handleFileOpen(ActionEvent event) throws IOException {
		File selectedFile = openFileChooser("Select Image");

		LOGGER.info("Started File Open File: {}", selectedFile);

		if (selectedFile == null) {
			// alert if the desired file had issues uploading & return early
			Alert nullFileAlert = new Alert(Alert.AlertType.INFORMATION, "No File Selected");
			nullFileAlert.show();
			LOGGER.error("File Open Resulted in NULL.");
			return;
		}

		try  {
			loadImageAndUpdateWorkspace(selectedFile);
			notificationsHandler.showFileOpenedNotification(selectedFile.getName(), this.workspaceHandler.getCurrentScene().getWindow());
		} catch (IOException e) {
			showErrorAlert("Unable to load image: ERROR: " + e.getMessage());
			LOGGER.error("Unable to open file");
		}

		LOGGER.info("Finished File Open. Opened File: {}", selectedFile);
	}

	private File openFileChooser(String title) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(title);
		setFileChooserExt(fileChooser);
		return fileChooser.showOpenDialog(null);
	}

	private void loadImageAndUpdateWorkspace(File selectedFile) throws IOException {
		Image image = new Image(selectedFile.toURI().toURL().toExternalForm(), true);
		waitForImageLoad(image, selectedFile);
		webServerHandler.updateCurrentFile(selectedFile);
	}

	private void waitForImageLoad(Image image, File selectedFile) {
		image.progressProperty().addListener((obs, oldProgress, newProgress) -> {
			if (newProgress.doubleValue() == 1.0) { // -> Image is loaded
				updateCurrentWorkspaceWithImage(selectedFile, image);
			}
		});
	}

	private void updateCurrentWorkspaceWithImage(File selectedFile, Image image) {
		Workspace currentWorkspace = this.workspaceHandler.getCurrentWorkspace();

		// apply image to the current open workspace
		currentWorkspace.getCanvasController().setCanvas(image);
		currentWorkspace.getCanvasModel().setChangesMade(true);
		currentWorkspace.setWorkspaceFile(selectedFile);
	}

	/**
	 * This method handles the 'save' action.
	 *  Image Saving Process
	 * 	1. Create an empty WritableImage object with desired dimensions
	 * 	2. Take a canvas snapshot (This will capture every pixel on the canvas)
	 * 	3. Create a BufferedImage object using SwingFXUtils.fromFXImage(writableImage, null)
	 * 	4. Create a File object that contains the desired save path
	 * 	5. Call ImageIO.write()
	 *
	 * @param event takes an ActionEvent in case method is used by UI component
	 * */
	@FXML
	public void saveCurrentWorkspaceToFile(ActionEvent event) throws IOException {
		Workspace currentWorkspace = workspaceHandler.getCurrentWorkspace();
		File currentFile = currentWorkspace.getWorkspaceFile();
		// Check if there is a current file opened
		if (currentFile == null) {
			// Redirect request to handleFileSaveAs
			handleFileSaveAs(event);
			return;
		}

		notificationsHandler.showFileSavedNotification(currentFile.getName(), workspaceHandler.getCurrentScene().getWindow());

		try {
			// uses threading to run save task concurrently to JavaFX thread
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					saveImageToFile(currentWorkspace, currentFile);
					resetAutoSaveTimer();
				}
			});
		} catch (Exception e) {
			showErrorAlert("Unable to save the current image: ERROR: " + e.getMessage());
		}
	}

	private void saveImageToFile(Workspace currentWorkspace, File currentFile) {
		String fileExt = getFileExt(currentFile);
		currentWorkspace.getCanvasController().saveImageFromCanvas(currentFile, fileExt);
		currentWorkspace.setWorkspaceFile(currentFile);
		webServerHandler.updateCurrentFile(currentFile);
	}



	/**
	 * This method handles the 'save-as' action.
	 * This differs from the 'save' action, in that it doesn't overwrite the current working file. Instead, it allows the user
	 * to save the contents of the file into a new file with a different location, and/or a different file extension.
	 *  Image Saving Process
	 * 	1. Create an empty WritableImage object with desired dimensions
	 * 	2. Take a canvas snapshot (This will capture every pixel on the canvas)
	 * 	3. Create a BufferedImage object using SwingFXUtils.fromFXImage(writableImage, null)
	 * 	4. Create a File object that contains the desired save path
	 * 	5. Call ImageIO.write()
	 *
	 * @param event takes an ActionEvent in case method is used by UI component
	 * */
	@FXML
	private void handleFileSaveAs(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		setFileChooserExt(fileChooser);
		fileChooser.setTitle("Save Image");
		fileChooser.setInitialFileName("Untitled");

		Optional<File> file = Optional.ofNullable(fileChooser.showSaveDialog(null));

		if (file.isEmpty()) {
			showInfoAlert("No File Selected, please select a valid '.png' OR '.jpg' OR '.bmp' file to continue.");
			return;
		}

		String fileExt = getFileExt(file.get());
		Workspace currentWorkspace = this.workspaceHandler.getCurrentWorkspace();

		notificationsHandler.showFileSavedNotification(file.get().getName(), workspaceHandler.getCurrentScene().getWindow());

		if (currentWorkspace.getWorkspaceFile() != null && !handleSaveAsDiffExt(fileExt)) { // file has been previously saved
			return;
		}

		currentWorkspace.getCanvasController().saveImageFromCanvas(file.get(), fileExt);
		currentWorkspace.setWorkspaceFile(file.get());
		webServerHandler.updateCurrentFile(file.get());
	}

	private void showInfoAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
		alert.show();
	}

	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR, message);
		alert.show();
	}

	private void resetAutoSaveTimer() {
		// Reset timer on save
		seconds = (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60;
		restartAutoSaveTimer();
		autoSave.restartAutoSaveService();
	}

	// data loss prevention if user attempts to save image as a different file extension
	private boolean handleSaveAsDiffExt(String newFileExt) {
		Workspace currentWorkspace = this.workspaceHandler.getCurrentWorkspace();
		String currentFileExt = getFileExt(currentWorkspace.getWorkspaceFile());

		// Check if file is being saved with a different extension
		if (!newFileExt.equals(currentFileExt)) {
			// Show alert that data may be lost
			ButtonType bT = showDataLossAlert(currentFileExt, newFileExt);
            return bT == ButtonType.OK;
		} else {
			// Extensions are the same, allow user to save
			return true;
		}
	}

	private void setFileChooserExt(FileChooser fileChooser) {
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png")
				,new FileChooser.ExtensionFilter("JPEG (*.jpg)", "*.jpg")
				,new FileChooser.ExtensionFilter("Bitmap (*.bmp, *.dib)", "*.bmp", "*.dib")
		);

		File paintFileDir = createFileChooserDir(null, null);
		fileChooser.setInitialDirectory(paintFileDir);
	}





	// data loss alert, waits for user input before continuing
	private ButtonType showDataLossAlert(String currFile, String newFile) {
		Alert dataLossAlert = new Alert(Alert.AlertType.WARNING);
		dataLossAlert.setTitle("WARNING: POTENTIAL DATA LOSS");
		dataLossAlert.setContentText("ALERT: You are about to save a new file with different extension than that of the original file." +
				"This action may lead to the irreversible loss of image data, going from extension: " + currFile +
				" to extension: " + newFile + " If you wish to continue press OK."
		);
		dataLossAlert.getButtonTypes().add(ButtonType.CANCEL);
		Optional<ButtonType> userResponse =  dataLossAlert.showAndWait();
		ButtonType buttonType = userResponse.orElse(ButtonType.CANCEL);
		dataLossAlert.showAndWait();

		return buttonType;
	}


	/**
	 * Gets file ext.
	 *
	 * @param file the file
	 * @return the file ext
	 */
	public String getFileExt(File file) {
		LOGGER.info("INFO HERE");
		LOGGER.warn("WARN HERE");
		LOGGER.error("ERROR HERE");
		String filePath = file.getAbsolutePath();
		if (filePath.lastIndexOf(".") == -1) { // no occurrence of '.' symbol
			return "";
		}
		return filePath.substring(filePath.lastIndexOf(".") + 1); // Get string val after the last '.'
	}

	/**
	 * Create file chooser dir file.
	 *
	 * @param rootName the root name
	 * @param dirName  the dir name
	 * @return the file
	 */
// if no root name or directory name specified use default values
	public File createFileChooserDir(String rootName, String dirName) {
		if (rootName == null) {
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


	/**
	 * Handle new file.
	 *
	 * @param actionEvent the action event
	 * @throws IOException the io exception
	 */
// create a temp file when user creates a 'blank' project rather than opening a pre-existing file
	public void handleNewFile(ActionEvent actionEvent) throws IOException {
		Path tempFile = Files.createTempFile(Files.createTempDirectory("temp-dir"), "testData-", ".txt");

		File newFile = tempFile.toFile();
		this.workspaceHandler.setCurrentFile(newFile);
	}

	/**
	 * Initialize.
	 */
	@FXML
	public void initialize() {
		// event handlers for autosave
		timerLabel.setOnMouseClicked(this::handleTimerDialog);
	}

	private void handleTimerDialog(MouseEvent mouseEvent) {
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
			System.out.println(e.getMessage());
			Alert autoSaveDialogAlert = new Alert(Alert.AlertType.ERROR, "Unable to save AutoSave settings at this time. Please try again later. ERROR WITH AUTOSAVE SETTINGS: " + e.getMessage());
			autoSaveDialogAlert.showAndWait();
		}
	}

	// if user DID NOT click 'APPLY' return early
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
	private int prevTimerLen = -1;
	private boolean isTimerHidden = false;
	private final Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
		seconds--;
		if (!isTimerHidden) { // Check if the timer should be hidden, if hidden keep timer going
			timerLabel.setText(formatTimerLength(seconds));
		}
	}));

	private String formatTimerLength(int timeInSeconds) {
		float time = (float) (timeInSeconds / 60.0);
		int minutes = (int) time;
		double timeDifference = (time - minutes);
		int seconds = (int) Math.round(timeDifference * 60);

		// Convert seconds to minutes
		// i.e. timeInSeconds = 645 -> minutes = 10.75
		return minutes + ":" + seconds;
	}

	/**
	 * Hide auto save timer.
	 *
	 * @param showTimer the show timer
	 */
	public void hideAutoSaveTimer(boolean showTimer) {
		if (!showTimer) {
			isTimerHidden = true;
			timerLabel.setText("Auto Save");
		} else {
			if (prevTimerLen < 0) {
				prevTimerLen = (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60;
			}

			isTimerHidden = false;
		}
	}

	/**
	 * Restart auto save timer.
	 */
	public void restartAutoSaveTimer() {
		timer.stop();
		timer.playFromStart();
	}

	/**
	 * Start auto save timer.
	 */
	public void startAutoSaveTimer() {
		int autoSaveInterval = (int) this.workspaceHandler.getSettingStateModel().getAutoSaveInterval() * 60;
		handleRunningTimer(autoSaveInterval);

		seconds = autoSaveInterval;
		timer.setCycleCount(autoSaveInterval); // seconds -> minutes
		timer.play();
	}

	private void handleRunningTimer(int autoSaveInterval) {
        // Return early if timer is still running and the prev timer length is the same
        if (timer.getStatus() != Animation.Status.RUNNING && prevTimerLen != autoSaveInterval) { // Use previous timer if val hasn't changed
			timer.stop();
			prevTimerLen = autoSaveInterval;
        }
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

	/**
	 * On key pressed undo btn.
	 *
	 * @param keyEvent the key event
	 */
	public void onKeyPressedUndoBtn(KeyEvent keyEvent) {
		this.workspaceHandler.getCurrentWorkspace().handleUndoAction();
	}

	/**
	 * On key pressed redo btn.
	 *
	 * @param keyEvent the key event
	 */
	public void onKeyPressedRedoBtn(KeyEvent keyEvent) {
		this.workspaceHandler.getCurrentWorkspace().handleRedoAction();
	}

	/**
	 * Gets default dirname.
	 *
	 * @return the default dirname
	 */
	public String getDEFAULT_DIRNAME() { return DEFAULT_DIRNAME; }

	/**
	 * Gets default root name.
	 *
	 * @return the default root name
	 */
	public String getDEFAULT_ROOT_NAME() { return DEFAULT_ROOT_NAME; }


	/**
	 * Gets load help dialog.
	 *
	 * @param actionEvent the action event
	 * @throws IOException the io exception
	 */
	public void getLoadHelpDialog(ActionEvent actionEvent) throws IOException {
		this.helpAboutModel.loadHelpMenu();
	}

	/**
	 * Gets load about dialog.
	 *
	 * @param actionEvent the action event
	 * @throws IOException the io exception
	 */
	public void getLoadAboutDialog(ActionEvent actionEvent) throws IOException {
		this.helpAboutModel.loadAboutMenu();
	}

	/**
	 * Gets web server handler.
	 *
	 * @return the web server handler
	 */
	public WebServerHandler getWebServerHandler() {
		return webServerHandler;
	}

	/**
	 * Sets web server handler.
	 *
	 * @param webServerHandler the web server handler
	 */
	public void setWebServerHandler(WebServerHandler webServerHandler) {
		this.webServerHandler = webServerHandler;
	}

	/**
	 * Gets auto save controller.
	 *
	 * @return the auto save controller
	 */
	public AutoSaveController getAutoSaveController() {
		return autoSaveController;
	}

	/**
	 * Sets auto save controller.
	 *
	 * @param autoSaveController the auto save controller
	 */
	public void setAutoSaveController(AutoSaveController autoSaveController) {
		this.autoSaveController = autoSaveController;
	}

	/**
	 * Gets auto save.
	 *
	 * @return the auto save
	 */
	public AutoSave getAutoSave() {
		return autoSave;
	}

	/**
	 * Sets auto save.
	 *
	 * @param autoSave the auto save
	 */
	public void setAutoSave(AutoSave autoSave) {
		this.autoSave = autoSave;
	}

	/**
	 * Gets canvas model.
	 *
	 * @return the canvas model
	 */
	public CanvasModel getCanvasModel() {
		return canvasModel;
	}

	/**
	 * Sets canvas model.
	 *
	 * @param canvasModel the canvas model
	 */
	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	/**
	 * Sets help about model.
	 *
	 * @param helpAboutModel the help about model
	 */
	public void setHelpAboutModel(HelpAboutModel helpAboutModel) {
		this.helpAboutModel = helpAboutModel;
	}

	/**
	 * Sets current workspace model.
	 *
	 * @param workspaceHandler the workspace handler
	 */
	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	/**
	 * Gets notifications handler.
	 *
	 * @return the notifications handler
	 */
	public NotificationsHandler getNotificationsHandler() {
		return notificationsHandler;
	}

	/**
	 * Sets notifications handler.
	 *
	 * @param notificationsHandler the notifications handler
	 */
	public void setNotificationsHandler(NotificationsHandler notificationsHandler) {
		this.notificationsHandler = notificationsHandler;
	}
}
