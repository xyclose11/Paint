package com.paint.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import javafx.event.ActionEvent;
import java.io.IOException;
public class HelpMenuController {

    private Stage stage;
    private static Scene mainScene; // Must be static to remember prev scene

    // Stage -> Scene -> Root node -> Leaf Nodes
    // Handle the opening of the options (Help) menu
    @FXML
    private void switchToOptionScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/OptionsScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        // Remember the last scene to be able to switch back
        mainScene = stage.getScene();
        System.out.println(mainScene);

        // Set new scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void switchToMainScene(ActionEvent event) {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        System.out.println(mainScene);

        // Set scene back to main
        stage.setScene(mainScene);
        stage.show();
    }



}
