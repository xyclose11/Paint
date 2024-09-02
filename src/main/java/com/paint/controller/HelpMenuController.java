package com.paint.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import java.io.IOException;
public class HelpMenuController {

    private Stage stage;
    private static Scene mainScene; // Must be static to remember prev scene
    private Dialog<String> aboutDialog;

    // Stage -> Scene -> Root node -> Leaf Nodes
    // Handle the opening of the options (Help) menu
    @FXML
    private void switchToOptionScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/OptionsScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        // Remember the last scene to be able to switch back
        mainScene = stage.getScene();

        // Set new scene
        Scene scene = new Scene(root);
        scene.getStylesheets().add(mainScene.getStylesheets().get(0));

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

    @FXML
    private void loadAboutDialog(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HelpMenu.fxml"));
        DialogPane dialogPane = fxmlLoader.load();

        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About Pain(t)");
        aboutAlert.setDialogPane(dialogPane);
        aboutAlert.show();

    }



}
