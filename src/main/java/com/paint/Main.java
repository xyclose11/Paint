package com.paint;

import com.paint.controller.*;
import com.paint.handler.SelectionHandler;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.*;
import com.paint.resource.TransformableNode;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    // Instantiate models for future use
    private final CanvasModel canvasModel = new CanvasModel();
    private final PaintStateModel paintStateModel = new PaintStateModel();
    private SceneStateModel sceneStateModel = null;
    private HelpAboutModel helpAboutModel = new HelpAboutModel();
    private InfoCanvasModel infoCanvasModel = new InfoCanvasModel();
    private SettingStateModel settingStateModel = new SettingStateModel();
    private TabModel tabModel = new TabModel();
    private WorkspaceHandler workspaceHandler = new WorkspaceHandler();
    private SelectionHandler selectionHandler = new SelectionHandler();

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
        toolMenuController.setCurrentWorkspaceModel(workspaceHandler);

        UtilityController utilityController = utilityMenuLoader.getController();
        utilityController.setCurrentWorkspaceModel(workspaceHandler);

        // Set bottom
        FXMLLoader infoBarLoader = new FXMLLoader(getClass().getResource("/view/InfoBar.fxml"));

        rootLayout.setBottom(infoBarLoader.load());

        InfoController infoController = infoBarLoader.getController();

        Scene scene = new Scene(rootLayout, 1225, 735);
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
        canvasController.setCurrentWorkspaceModel(workspaceHandler);

        selectionHandler.setPaintStateModel(paintStateModel);
        selectionHandler.setCurrentWorkspaceModel(workspaceHandler);
        canvasController.setSelectionHandler(selectionHandler);

        tabController.setCanvasModel(canvasModel);
        tabController.setTabModel(tabModel);
        tabController.setPaintStateModel(paintStateModel);
        tabController.setInfoCanvasModel(infoCanvasModel);
        tabController.setSettingStateModel(settingStateModel);
        tabController.setCurrentWorkspaceModel(workspaceHandler);

        // Maintains the state of the current workspace in focus
        canvasWrapper.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            // Create a simple Rectangle Node
            Circle rect = new Circle(350, 350, 45);
            rect.setFill(Color.PINK);

            // Create the TransformableNode by wrapping the Rectangle
            TransformableNode transformableRect = new TransformableNode(rect, this.workspaceHandler.getCurrentWorkspace().getCanvasController());
            this.workspaceHandler.getCurrentWorkspace().getCanvasController().getDrawingPane().getChildren().add(transformableRect);

            // Change active workspace
            Workspace workspace = this.workspaceHandler.getWorkspaceList().get(newValue);
            this.workspaceHandler.setCurrentWorkspace(workspace);

            File currentWorkspaceFile = workspace.getWorkspaceFile();
            this.workspaceHandler.setCurrentFile(currentWorkspaceFile);

            // Exit user from transformation mode
//            this.paintStateModel.enableTransformations(false, this.workspaceHandler.getCurrentWorkspace().getCanvasController().getDrawingPane());

            // Set active tab
            this.tabModel.setCurrentTab(canvasWrapper.getTabs().get((Integer) newValue));
        }));

        tabModel.setTabPane(canvasWrapper);

        infoController.setCanvasModel(canvasModel);
        infoController.setInfoCanvasModel(infoCanvasModel);
        infoController.setPaintStateModel(paintStateModel);
        infoController.setCurrentWorkspaceModel(workspaceHandler);

        paintStateModel.setInfoCanvasModel(infoCanvasModel);
        paintStateModel.setCurrentWorkspaceModel(workspaceHandler);

        workspaceHandler.setSettingStateModel(settingStateModel);
        workspaceHandler.setInfoCanvasModel(infoCanvasModel);
        workspaceHandler.setTabModel(tabModel);
        workspaceHandler.setPaintStateModel(paintStateModel);


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
                            workspaceHandler.getCurrentWorkspace().handleUndoAction();
                            // When undoing add to redo stack
                            keyEvent.consume();
                            break;
                        case Y: // Redo
                            workspaceHandler.getCurrentWorkspace().handleRedoAction();
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

        // Set 'smart-save' event handlers on main stage
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                 // Check if file has been saved
	            boolean isSaved = false;
	            try {
                    // If no tabs active allow user to exit without alerting
                    if (workspaceHandler.getSize() == 0) {
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

                    ((Button)fileNotSavedAlert.getDialogPane().lookupButton(ButtonType.OK)).setText("Don't Save"); // Change default btn text

                    ButtonType alertResult = fileNotSavedAlert.showAndWait().orElse(ButtonType.CANCEL);

                    if (alertResult == ButtonType.OK) { // User wants to close without saving
                        primaryStage.close();
                    }
                    if (alertResult.getText() == "Save") { // Save file
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