package com.paint;

import com.paint.controller.*;
import com.paint.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    // Instantiate models for future use
    private final CanvasModel canvasModel = new CanvasModel();
    private final PaintStateModel paintStateModel = new PaintStateModel();
    private SceneStateModel sceneStateModel = null;
    private HelpAboutModel helpAboutModel = new HelpAboutModel();
    private InfoCanvasModel infoCanvasModel = new InfoCanvasModel();
    private SettingStateModel settingStateModel = new SettingStateModel();

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane rootLayout = new BorderPane(); // Contains the main scene/content

        // Set Center
        FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));
        rootLayout.setCenter(canvasLoader.load());

        CanvasController canvasController = canvasLoader.getController();


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
        FXMLLoader optionsLoader = new FXMLLoader(getClass().getResource("/view/OptionsScene.fxml"));
        optionsLoader.load();

        HelpMenuController helpMenuController = optionsLoader.getController();

        Scene scene = new Scene(rootLayout, 1225, 735);
        sceneStateModel = new SceneStateModel(scene);

        // Set controller models
        utilityController.setHelpAboutModel(helpAboutModel);

        toolMenuController.setPaintStateModel(paintStateModel);
        toolMenuController.setSceneStateModel(sceneStateModel);

        canvasController.setCanvasModel(canvasModel);
        canvasController.setPaintStateModel(paintStateModel);
        canvasController.setInfoCanvasModel(infoCanvasModel);
        canvasController.setSettingStateModel(settingStateModel);
        canvasController.setSceneStateModel(sceneStateModel);

        System.out.println(settingStateModel);
        helpMenuController.setSettingStateModel(settingStateModel);

        infoController.setCanvasModel(canvasModel);
        infoController.setInfoCanvasModel(infoCanvasModel);

        // Update main controller state
        sceneStateModel.setHelpMenuController(helpMenuController);
        sceneStateModel.setMainScene(scene);
        sceneStateModel.setPrimarystage(primaryStage);

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
        primaryStage.setOnHiding(windowEvent -> {
           // Check if file has been saved
            boolean isSaved = canvasController.fileSavedRecently();
            if (isSaved) {
                // File saved recently -> OK to close
                return;
            } else {
                // File not saved recently -> Alert user
                Alert fileNotSavedAlert = new Alert(Alert.AlertType.WARNING, """
                        WARNING: You are about to close a file that has not been saved with your most recent changes. Are
                        you sure you want to close?
                        """);
                fileNotSavedAlert.setTitle("WARNING: File not saved");
                fileNotSavedAlert.setHeaderText("");

                ButtonType saveFileBtn = new ButtonType("Save File", ButtonBar.ButtonData.APPLY); // Add a 'save' btn on dialog
                ButtonType saveFileAsBtn = new ButtonType("Save File As", ButtonBar.ButtonData.APPLY); // Add a 'save as' btn on dialog

                fileNotSavedAlert.getButtonTypes().add(saveFileBtn);
                fileNotSavedAlert.getButtonTypes().add(saveFileAsBtn);

                Optional<ButtonType> userInput = fileNotSavedAlert.showAndWait();

                if (userInput.isPresent() && userInput.get() == ButtonType.OK) {
                    System.out.println("OK");
                } else {
                    System.out.println("NOT OK");

                }
            }
        });
    }
    
    public static void main(String[] args) {
        launch();
    }
}