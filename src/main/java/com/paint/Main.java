package com.paint;

import com.paint.view.SharedLayout;
import com.paint.view.UtilityMenu;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        SharedLayout sharedLayout = new SharedLayout();
        VBox rootLayout = sharedLayout.Init();

        Scene primaryScene = new Scene(rootLayout, 600, 450);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}