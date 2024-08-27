package com.paint.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PaintController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    protected void onFileSaveButtonClick() {

    }
}