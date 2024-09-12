package com.paint;

import com.paint.controller.*;
import com.paint.model.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane rootLayout = new BorderPane(); // Contains the main scene/content

        // Set Center
        FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));

        HBox canvasView = canvasLoader.load();

        FXMLLoader tabPaneWrapperLoader = new FXMLLoader(getClass().getResource("/view/TabPaneWrapper.fxml"));
        TabPane canvasWrapper = tabPaneWrapperLoader.load();

        Tab initialTab = canvasWrapper.getTabs().get(0);

        initialTab.setContent(canvasView);

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

        UtilityController utilityController = utilityMenuLoader.getController();

        // Instantiate the CanvasController inside utilityController
        utilityController.setCanvasController(canvasController);

        // Set bottom
        FXMLLoader infoBarLoader = new FXMLLoader(getClass().getResource("/view/InfoBar.fxml"));

        rootLayout.setBottom(infoBarLoader.load());

        InfoController infoController = infoBarLoader.getController();

        // Load optionsScene but hide it
//        FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("/view/OptionsScene.fxml"));
//        optionsLoader.load();

//        HelpMenuController helpMenuController = optionsLoader.getController();

        Scene scene = new Scene(rootLayout, 1225, 735);
        sceneStateModel = new SceneStateModel(scene);

        // Set controller models
        utilityController.setHelpAboutModel(helpAboutModel);
        utilityController.setCanvasModel(canvasModel);

        toolMenuController.setPaintStateModel(paintStateModel);
        toolMenuController.setSceneStateModel(sceneStateModel);
        toolMenuController.setCanvasModel(canvasModel);

        canvasController.setCanvasModel(canvasModel);
        canvasController.setPaintStateModel(paintStateModel);
        canvasController.setInfoCanvasModel(infoCanvasModel);
        canvasController.setSettingStateModel(settingStateModel);
        canvasController.setSceneStateModel(sceneStateModel);
        canvasController.setTabModel(tabModel);

        tabController.setCanvasModel(canvasModel);
        tabController.setTabModel(tabModel);
        tabModel.setTabPane(canvasWrapper);
        tabModel.setNewTab(initialTab);

        canvasModel.setCurrentCanvasController(canvasController);
//        helpMenuController.setSettingStateModel(settingStateModel);

        infoController.setCanvasModel(canvasModel);
        infoController.setInfoCanvasModel(infoCanvasModel);
        infoController.setPaintStateModel(paintStateModel);

        paintStateModel.setInfoCanvasModel(infoCanvasModel);
        paintStateModel.setCanvasController(canvasController);

        // Update main controller state
//        sceneStateModel.setHelpMenuController(helpMenuController);
//        sceneStateModel.setMainScene(scene);
//        sceneStateModel.setPrimarystage(primaryStage);

        // Add style sheets
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        // Set 'smart-save' event handlers on main stage
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                 // Check if file has been saved
	            boolean isSaved = false;
	            try {
		            isSaved = canvasController.isFileSavedRecently();
	            } catch (IOException e) {
		            throw new RuntimeException(e);
	            }
	            if (isSaved || canvasModel.isFileBlank()) {
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