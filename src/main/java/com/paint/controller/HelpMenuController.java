package com.paint.controller;

import com.paint.model.HelpAboutModel;
import com.paint.model.SettingStateModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is used to handle the UI state for the settings view
 *
 * @since 1.1
 * */
public class HelpMenuController {

    /**
     * The Java pun.
     */
    @FXML
    public Label javaPun;

    /**
     * The Java pun answer.
     */
    @FXML
    public Label javaPunAnswer;

    private Stage stage;
    private static Scene mainScene; // Must be static to remember prev scene
    private Dialog<String> aboutDialog;
    private SettingStateModel settingStateModel;

    /**
     * Gets setting state model.
     *
     * @return the setting state model
     */
    public SettingStateModel getSettingStateModel() {
        return settingStateModel;
    }

    /**
     * Sets setting state model.
     *
     * @param settingStateModel the setting state model
     */
    public void setSettingStateModel(SettingStateModel settingStateModel) {
        this.settingStateModel = settingStateModel;
    }

    private HelpAboutModel helpAboutModel;

    // Initialize HelpAboutModel
    private void setHelpAboutModel(HelpAboutModel helpAboutModel) {
        this.helpAboutModel = helpAboutModel;
    }

    // Stage -> Scene -> Root node -> Leaf Nodes
    // Handle the opening of the options (Help) menu
    @FXML
    private void switchToOptionScene(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/OptionsScene.fxml"));
        Parent root = fxmlLoader.load();
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

        // Set scene back to main
        stage.setScene(mainScene);
        stage.show();
    }

    @FXML
    private void loadAboutDialog(ActionEvent event) throws IOException {
        this.helpAboutModel.loadAboutMenu();
    }

    @FXML
    private void loadHelpDialog(ActionEvent actionEvent) throws IOException {
        this.helpAboutModel.loadHelpMenu();
    }

    @FXML
    private void handleMouseClickedPun() {
        this.javaPunAnswer.setOpacity(1.0);
    }

    @FXML
    private void initialize(){
        // Set HelpAboutModel
        this.setHelpAboutModel(new HelpAboutModel());
    }
}
