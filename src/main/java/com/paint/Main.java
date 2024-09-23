package com.paint;

import com.paint.controller.*;
import com.paint.model.*;
import com.paint.resource.AutoSave;
import com.paint.resource.WebServerHandler;
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

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    // Instantiate models for future use
    private final CanvasModel canvasModel = new CanvasModel();
    private final PaintStateModel paintStateModel = new PaintStateModel();
    private SceneStateModel sceneStateModel = null;
    private HelpAboutModel helpAboutModel = new HelpAboutModel();
    private InfoCanvasModel infoCanvasModel = new InfoCanvasModel();
    private SettingStateModel settingStateModel = new SettingStateModel();
    private TabModel tabModel = new TabModel();
    private CurrentWorkspaceModel currentWorkspaceModel = new CurrentWorkspaceModel();
    private SelectionHandler selectionHandler = new SelectionHandler();

    // Threading
    private final AutoSave autoSaveService = new AutoSave();

    @Override
    public void start(Stage primaryStage) throws Exception {
        final int INITIAL_RES_X = 1200; // Initial resolution vals
        final int INITIAL_RES_Y = 900;

        BorderPane rootLayout = new BorderPane(); // Contains the main scene/content

        rootLayout.setPrefWidth(INITIAL_RES_X);
        rootLayout.setPrefHeight(INITIAL_RES_Y);
        // Set Center
        FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));

        HBox canvasView = canvasLoader.load();


        FXMLLoader tabPaneWrapperLoader = new FXMLLoader(getClass().getResource("/view/TabPaneWrapper.fxml"));
        TabPane canvasWrapper = tabPaneWrapperLoader.load();

        rootLayout.setCenter(canvasWrapper);

        canvasModel.setCanvasView(canvasView);

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

        // Load ToolMenuController after load
        ToolMenuController toolMenuController = toolMenuLoader.getController();
        toolMenuController.setCurrentWorkspaceModel(currentWorkspaceModel);

        UtilityController utilityController = utilityMenuLoader.getController();
        utilityController.setCurrentWorkspaceModel(currentWorkspaceModel);

        // Set bottom
        FXMLLoader infoBarLoader = new FXMLLoader(getClass().getResource("/view/InfoBar.fxml"));

        rootLayout.setBottom(infoBarLoader.load());

        InfoController infoController = infoBarLoader.getController();

        Scene scene = new Scene(rootLayout, INITIAL_RES_X, INITIAL_RES_Y);
        sceneStateModel = new SceneStateModel(scene);

        // Set controller models
        utilityController.setHelpAboutModel(helpAboutModel);
        utilityController.setCanvasModel(canvasModel);

        toolMenuController.setPaintStateModel(paintStateModel);
        toolMenuController.setSceneStateModel(sceneStateModel);

        // Set the FXMLLoader for the Font tool menu
        FXMLLoader fontToolBarLoader = new FXMLLoader(getClass().getResource("/view/FontToolBar.fxml"));
        toolMenuController.setFontToolBarLoader(fontToolBarLoader);


        canvasController.setCanvasModel(canvasModel);
        canvasController.setPaintStateModel(paintStateModel);
        canvasController.setInfoCanvasModel(infoCanvasModel);
        canvasController.setSettingStateModel(settingStateModel);
        canvasController.setSceneStateModel(sceneStateModel);
        canvasController.setTabModel(tabModel);
        canvasController.setCurrentWorkspaceModel(currentWorkspaceModel);

        selectionHandler.setPaintStateModel(paintStateModel);
        selectionHandler.setCurrentWorkspaceModel(currentWorkspaceModel);
        canvasController.setSelectionHandler(selectionHandler);

        tabController.setCanvasModel(canvasModel);
        tabController.setTabModel(tabModel);
        tabController.setPaintStateModel(paintStateModel);
        tabController.setInfoCanvasModel(infoCanvasModel);
        tabController.setSettingStateModel(settingStateModel);
        tabController.setCurrentWorkspaceModel(currentWorkspaceModel);

        autoSaveService.setUtilityController(utilityController);
        utilityController.setAutoSave(autoSaveService);
        autoSaveService.startTimer();
        // Maintains the state of the current workspace in focus
        canvasWrapper.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            // Change active workspace
            Workspace workspace = this.currentWorkspaceModel.getWorkspaceList().get(newValue);
            this.currentWorkspaceModel.setCurrentWorkspace(workspace);

            File currentWorkspaceFile = workspace.getWorkspaceFile();
            this.currentWorkspaceModel.setCurrentFile(currentWorkspaceFile);

            // Exit user from transformation mode
            this.paintStateModel.setTransformable(false, this.currentWorkspaceModel.getCurrentWorkspace().getCanvasController().getDrawingPane());

            // Set active tab
            this.tabModel.setCurrentTab(canvasWrapper.getTabs().get((Integer) newValue));
        }));

        tabModel.setTabPane(canvasWrapper);

        infoController.setCanvasModel(canvasModel);
        infoController.setInfoCanvasModel(infoCanvasModel);
        infoController.setPaintStateModel(paintStateModel);
        infoController.setCurrentWorkspaceModel(currentWorkspaceModel);

        paintStateModel.setInfoCanvasModel(infoCanvasModel);
        paintStateModel.setCurrentWorkspaceModel(currentWorkspaceModel);

        currentWorkspaceModel.setSettingStateModel(settingStateModel);
        currentWorkspaceModel.setInfoCanvasModel(infoCanvasModel);
        currentWorkspaceModel.setTabModel(tabModel);
        currentWorkspaceModel.setPaintStateModel(paintStateModel);

        settingStateModel.setAutoSave(autoSaveService);

        // Load blank tab on startup
        tabController.createNewTab();


        // Add style sheets
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // UNDO SETUP START
        // UNDO SETUP END

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
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            keyEvent.consume(); // This prevents the event from going any further
                            break;
                        case Z: // Undo
                            currentWorkspaceModel.getCurrentWorkspace().handleUndoAction();
                            // When undoing add to redo stack
                            keyEvent.consume();
                            break;
                        case Y: // Redo
                            currentWorkspaceModel.getCurrentWorkspace().handleRedoAction();
                            // When redoing add to undo stack
                            keyEvent.consume();
                            break;
                        case C:
                            selectionHandler.copySelectionContent();
                            keyEvent.consume();
                            break;
                        case V:
                            selectionHandler.pasteClipboardImage(); // TODO handle different types of paste i.e. text, HTML, etc.
                            keyEvent.consume();
                            break;
                    }

                }

            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();

        WebServerHandler webServerHandler = new WebServerHandler();
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
                    if (currentWorkspaceModel.getSize() == 0) {
                        primaryStage.close();
                    } else {
                        CanvasController currentCanvasController = currentWorkspaceModel.getCurrentWorkspace().getCanvasController();
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

                    ((Button)fileNotSavedAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Don't Save"); // Change default btn text

                    ButtonType alertResult = fileNotSavedAlert.showAndWait().orElse(ButtonType.CANCEL);

                    if (alertResult == ButtonType.OK) { // User wants to close without saving
                        primaryStage.close();
                        webServerHandler.stopHttpServer();// Stop httpServer
                    }
                    if (Objects.equals(alertResult.getText(), "Save")) { // Save file
	                    try {
		                    utilityController.handleFileSave(null);
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

    public static void main(String[] args) {
        launch();
    }
}