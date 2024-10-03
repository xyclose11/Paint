package com.paint;

import com.paint.controller.*;
import com.paint.handler.NotificationsHandler;
import com.paint.handler.SelectionHandler;
import com.paint.handler.WebServerHandler;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.*;
import com.paint.resource.AutoSave;
import com.paint.resource.Workspace;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
	// Instantiate models for future use
	private final CanvasModel canvasModel = new CanvasModel();
	private final PaintStateModel paintStateModel = new PaintStateModel();
	private final HelpAboutModel helpAboutModel = new HelpAboutModel();
	private final InfoCanvasModel infoCanvasModel = new InfoCanvasModel();
	private final SettingStateModel settingStateModel = new SettingStateModel();
	private final TabModel tabModel = new TabModel();

	// Handlers
	private final WorkspaceHandler workspaceHandler = new WorkspaceHandler();
	private final SelectionHandler selectionHandler = new SelectionHandler();
	private final NotificationsHandler notificationsHandler = new NotificationsHandler();

	// Threading
	private final AutoSave autoSaveService = new AutoSave();

	// Logging
	protected static final Logger LOGGER = LogManager.getLogger();

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final WebServerHandler webServerHandler = new WebServerHandler();

		// Initial resolution vals
		final int INITIAL_RES_X = 1200;
		final int INITIAL_RES_Y = 900;

		BorderPane rootLayout = new BorderPane(); // Contains the main scene/content

		rootLayout.setPrefWidth(INITIAL_RES_X);
		rootLayout.setPrefHeight(INITIAL_RES_Y);

		// CanvasView
		FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));
		HBox canvasView = canvasLoader.load();

		// TabPaneWrapper View
		FXMLLoader tabPaneWrapperLoader = new FXMLLoader(getClass().getResource("/view/TabPaneWrapper.fxml"));
		TabPane canvasWrapper = tabPaneWrapperLoader.load();

		rootLayout.setCenter(canvasWrapper);

		// Controller instantiation
		CanvasController canvasController = canvasLoader.getController();
		TabController tabController = tabPaneWrapperLoader.getController();

		// Wrap topper, and set top
		FXMLLoader utilityMenuLoader = new FXMLLoader(getClass().getResource("/view/UtilityMenu.fxml"));
		FXMLLoader toolMenuLoader = new FXMLLoader(getClass().getResource("/view/ToolMenu.fxml"));

		VBox topWrapper = new VBox();
		topWrapper.setPadding(new Insets(0, 5, 0, 0)); // Set spacing for top in borderPane

		VBox utilityMenu = utilityMenuLoader.load();
		topWrapper.getChildren().addAll(utilityMenu, toolMenuLoader.load());
		rootLayout.setTop(topWrapper);

		// Controller instantiation
        ToolMenuController toolMenuController = toolMenuLoader.getController();
		UtilityController utilityController = utilityMenuLoader.getController();

		// Set bottom
		FXMLLoader infoBarLoader = new FXMLLoader(getClass().getResource("/view/InfoBar.fxml"));
		rootLayout.setBottom(infoBarLoader.load());
		InfoController infoController = infoBarLoader.getController();

		Scene scene = new Scene(rootLayout, INITIAL_RES_X, INITIAL_RES_Y);

		// Set controller models
		toolMenuController.setCurrentWorkspaceModel(workspaceHandler);

		utilityController.setCurrentWorkspaceModel(workspaceHandler);
		utilityController.setNotificationsHandler(notificationsHandler);

		utilityController.setHelpAboutModel(helpAboutModel);
		utilityController.setCanvasModel(canvasModel);
		utilityController.setWebServerHandler(webServerHandler);
		toolMenuController.setPaintStateModel(paintStateModel);

		// Set the FXMLLoader for the Font tool menu
		FXMLLoader fontToolBarLoader = new FXMLLoader(getClass().getResource("/view/FontToolBar.fxml"));
		toolMenuController.setFontToolBarLoader(fontToolBarLoader);

		// Handler model instantiation
		webServerHandler.setInfoController(infoController);
		workspaceHandler.setSettingStateModel(settingStateModel);
		workspaceHandler.setInfoCanvasModel(infoCanvasModel);
		workspaceHandler.setTabModel(tabModel);
		workspaceHandler.setPaintStateModel(paintStateModel);
		workspaceHandler.setCurrentScene(scene);
		workspaceHandler.setWebServerHandler(webServerHandler);

		selectionHandler.setPaintStateModel(paintStateModel);
		selectionHandler.setCurrentWorkspaceModel(workspaceHandler);

		// Controller model instantiation
		canvasController.setCanvasModel(canvasModel);
		canvasController.setPaintStateModel(paintStateModel);
		canvasController.setInfoCanvasModel(infoCanvasModel);
		canvasController.setTabModel(tabModel);
		canvasController.setCurrentWorkspaceModel(workspaceHandler);
		canvasController.setSelectionHandler(selectionHandler);

		tabController.setCanvasModel(canvasModel);
		tabController.setTabModel(tabModel);
		tabController.setPaintStateModel(paintStateModel);
		tabController.setInfoCanvasModel(infoCanvasModel);
		tabController.setSettingStateModel(settingStateModel);
		tabController.setCurrentWorkspaceModel(workspaceHandler);
		tabController.setWebServerHandler(webServerHandler);

		infoController.setCanvasModel(canvasModel);
		infoController.setInfoCanvasModel(infoCanvasModel);
		infoController.setPaintStateModel(paintStateModel);
		infoController.setCurrentWorkspaceModel(workspaceHandler);
		infoController.setNotificationsHandler(notificationsHandler);

		autoSaveService.setUtilityController(utilityController);
		utilityController.setAutoSave(autoSaveService);
		autoSaveService.startTimer();


		// Maintains the state of the current workspace in focus
		canvasWrapper.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
			// Change active workspace
			Workspace workspace = this.workspaceHandler.getWorkspaceList().get(newValue);
			this.workspaceHandler.setCurrentWorkspace(workspace);

			LOGGER.info("Current Workspace Changed to {}", workspace);

			File currentWorkspaceFile = workspace.getWorkspaceFile();
			this.workspaceHandler.setCurrentFile(currentWorkspaceFile);

			LOGGER.info("Current Workspace File: {}", currentWorkspaceFile);

			// Exit user from transformation mode
			if (this.paintStateModel.getCurrentNode() != null) {
				this.paintStateModel.getCurrentNode().exitTransformMode();
				LOGGER.info("Forcefully exited transform mode for current node");
			}

			// Set active tab
			this.tabModel.setCurrentTab(canvasWrapper.getTabs().get((Integer) newValue));
			LOGGER.info("Opened File: {} on Tab: {}", currentWorkspaceFile, newValue);

			// Update web server file on tab switch
			webServerHandler.updateCurrentFile(currentWorkspaceFile);

		}));

		tabModel.setTabPane(canvasWrapper);

		paintStateModel.setInfoCanvasModel(infoCanvasModel);
		paintStateModel.setCurrentWorkspaceModel(workspaceHandler);
		canvasModel.setCanvasView(canvasView);

		settingStateModel.setAutoSave(autoSaveService);

		// Load blank tab on startup
		tabController.createNewTab();

		// Add style sheets
		try {
			scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toString());
			LOGGER.info("Loaded CSS Style Sheets");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			LOGGER.fatal("Issue loading CSS Style Sheets in Main: {}",e);
		}

		// Setup key binding event listeners
		scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				// CTRL related key events
				if (keyEvent.isControlDown()) {
					switch (keyEvent.getCode()) {
						case N: // Create new tab -> file
							try {
								tabController.onKeyPressedNewFileTab(keyEvent);
								LOGGER.info("CTRL + N Pressed -> New Tab/File: {}", keyEvent);
							} catch (IOException e) {
								LOGGER.error("CTRL + N Key-bind error: {}",e);
								throw new RuntimeException(e);
							}
							keyEvent.consume(); // This prevents the event from going any further
							break;
						case Z: // Undo
							workspaceHandler.getCurrentWorkspace().handleUndoAction();
							LOGGER.info("CTRL + Z Pressed -> Undo Action: {}", keyEvent);
							// When undoing add to redo stack
							keyEvent.consume();
							break;
						case Y: // Redo
							workspaceHandler.getCurrentWorkspace().handleRedoAction();
							LOGGER.info("CTRL + Y Pressed -> Redo Action: {}", keyEvent);
							// When redoing add to undo stack
							keyEvent.consume();
							break;
						case C:
							selectionHandler.copySelectionContent();
							LOGGER.info("CTRL + C Pressed -> Copy Selection Action: {}", keyEvent);
							keyEvent.consume();
							break;
						case V:
							// Set tool to be selection
							paintStateModel.setCurrentToolType("paste");
							selectionHandler.pasteClipboardImage();
							LOGGER.info("CTRL + V Pressed -> Paste Action: {}", keyEvent);
							keyEvent.consume();
							break;
					}

				}

			}
		});

		primaryStage.setScene(scene);
		primaryStage.show();

		webServerHandler.createNewServer();
		webServerHandler.startHttpServer();


		// Set 'smart-save' event handlers on main stage
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				// Check if file has been saved
				boolean isSaved = false;
				try {
					// If no tabs active allow user to exit without alerting
					if (workspaceHandler.getSize() == 0) {
						LOGGER.info("Close application with current workspace size of {}", workspaceHandler.getSize());
						primaryStage.close();
					} else {
						CanvasController currentCanvasController = workspaceHandler.getCurrentWorkspace().getCanvasController();
						isSaved = currentCanvasController.isFileSavedRecently();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (isSaved) {
					// File saved recently -> OK to close || no file opened (blank)
					primaryStage.close();
				} else {
					// File not saved recently -> Alert user
					Alert fileNotSavedAlert = new Alert(Alert.AlertType.WARNING, """
							WARNING: You are about to close a file that has not been saved with your most recent changes. Are
							you sure you want to close?
							""");
					fileNotSavedAlert.setTitle("WARNING: File not saved");
					fileNotSavedAlert.setHeaderText("");

					ButtonType saveFileBtn = new ButtonType("Save", ButtonBar.ButtonData.APPLY); // Add a 'save' btn on dialog

					fileNotSavedAlert.getButtonTypes().add(saveFileBtn);
					fileNotSavedAlert.getButtonTypes().add(ButtonType.CANCEL);

					((Button) fileNotSavedAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Don't Save"); // Change default btn text

					ButtonType alertResult = fileNotSavedAlert.showAndWait().orElse(ButtonType.CANCEL);

					if (alertResult == ButtonType.OK) { // User wants to close without saving
						primaryStage.close();
						webServerHandler.stopHttpServer();// Stop httpServer
					}
					if (Objects.equals(alertResult.getText(), "Save")) { // Save file
						try {
							utilityController.saveCurrentWorkspaceToFile(null);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						event.consume(); // Consuming the event here prevents the 'exit' event from closing the application
					}

					if (alertResult == ButtonType.CANCEL) { // Cancel operation
						event.consume(); // Consuming the event here prevents the 'exit' event from closing the application
					}
				}
			}
		});
	}
}