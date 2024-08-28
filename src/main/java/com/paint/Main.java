package com.paint;

import com.paint.controller.UtilityController;
import com.paint.view.CanvasView;
import com.paint.view.SharedLayout;
import com.paint.view.UtilityMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SharedLayout sharedLayout = new SharedLayout();
        BorderPane rootLayout = sharedLayout.getLayout();
        UtilityMenu utilityMenu = sharedLayout.getUtilityMenu();
        CanvasView canvasView = sharedLayout.getCanvasView();
        new UtilityController(utilityMenu, canvasView);

        Scene primaryScene = new Scene(rootLayout, 725, 550);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}