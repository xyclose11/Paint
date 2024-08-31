package com.paint;

import com.paint.controller.CanvasController;
import com.paint.controller.InfoController;
import com.paint.controller.UtilityController;
import com.paint.model.CanvasModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private CanvasModel canvasModel = new CanvasModel();

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane rootLayout = new BorderPane();

        // Set Center
        FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));
        rootLayout.setCenter(canvasLoader.load());
        CanvasController canvasController = canvasLoader.getController();
        canvasController.setCanvasModel(canvasModel);

        // Wrap topper, and set top
        FXMLLoader utilityMenuLoader = new FXMLLoader(getClass().getResource("/view/UtilityMenu.fxml"));
        FXMLLoader toolMenuLoader = new FXMLLoader(getClass().getResource("/view/ToolMenu.fxml"));

        VBox topWrapper = new VBox();
        HBox temp = utilityMenuLoader.load();
        topWrapper.getChildren().addAll(temp, toolMenuLoader.load());
        rootLayout.setTop(topWrapper);

        UtilityController utilityController = utilityMenuLoader.getController();

        // Instantiate the CanvasController inside utilityController
        utilityController.setCanvasController(canvasController);

        // Set bottom
        FXMLLoader infoBarLoader = new FXMLLoader(getClass().getResource("/view/InfoBar.fxml"));

        rootLayout.setBottom(infoBarLoader.load());
        InfoController infoController = infoBarLoader.getController();
        infoController.setCanvasModel(canvasModel);

        Scene scene = new Scene(rootLayout, 1225, 735);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}