package com.paint;

import com.paint.controller.CanvasController;
import com.paint.controller.InfoController;
import com.paint.controller.ToolMenuController;
import com.paint.controller.UtilityController;
import com.paint.model.CanvasModel;
import com.paint.model.PaintStateModel;
import com.paint.model.SceneStateModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    // Instantiate models for future use
    private final CanvasModel canvasModel = new CanvasModel();
    private final PaintStateModel paintStateModel = new PaintStateModel();
    private SceneStateModel sceneStateModel = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane rootLayout = new BorderPane(); // Contains the main scene/content

        // Set Center
        FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));
        rootLayout.setCenter(canvasLoader.load());

        CanvasController canvasController = canvasLoader.getController();
        canvasController.setCanvasModel(canvasModel);
        canvasController.setPaintStateModel(paintStateModel);

        // Wrap topper, and set top
        FXMLLoader utilityMenuLoader = new FXMLLoader(getClass().getResource("/view/UtilityMenu.fxml"));
        FXMLLoader toolMenuLoader = new FXMLLoader(getClass().getResource("/view/ToolMenu.fxml"));

        VBox topWrapper = new VBox();
        HBox temp = utilityMenuLoader.load();
        topWrapper.getChildren().addAll(temp, toolMenuLoader.load());
        rootLayout.setTop(topWrapper);

        // Load ToolMenuController after load
        ToolMenuController toolMenuController = toolMenuLoader.getController();
        toolMenuController.setPaintStateModel(paintStateModel);

        UtilityController utilityController = utilityMenuLoader.getController();

        // Instantiate the CanvasController inside utilityController
        utilityController.setCanvasController(canvasController);

        // Set bottom
        FXMLLoader infoBarLoader = new FXMLLoader(getClass().getResource("/view/InfoBar.fxml"));

        rootLayout.setBottom(infoBarLoader.load());

        InfoController infoController = infoBarLoader.getController();
        infoController.setCanvasModel(canvasModel);

        Scene scene = new Scene(rootLayout, 1225, 735);
        sceneStateModel = new SceneStateModel(scene);

        // Add style sheets
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toString());
        } catch (Exception e) {
            // TODO MORE THOROUGH ERROR HANDLING
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}