package com.paint.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.io.IOException;

public class HelpAboutModel {

	public void loadAboutMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AboutMenu.fxml"));
		DialogPane dialogPane = fxmlLoader.load();

		Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
		aboutAlert.setTitle("About Pain(t)");
		aboutAlert.setDialogPane(dialogPane);
		aboutAlert.show();
	}

	public void loadHelpMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HelpMenu.fxml"));
		DialogPane dialogPane = fxmlLoader.load();

		Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
		aboutAlert.setTitle("Help with Pain(t)");
		aboutAlert.setDialogPane(dialogPane);
		aboutAlert.show();
	}
}
