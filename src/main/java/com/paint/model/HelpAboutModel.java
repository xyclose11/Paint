package com.paint.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.io.IOException;

/**
 * This model is used to store, show, and hide the 'About' and 'Help' menu FXML.
 *
 * @since 1.2
 * */
public class HelpAboutModel {

	/**
	 * This method loads the AboutMenu FXML file, creates the {@code DialogPane} and shows the Alert
	 * for the About Menu.
	 *
	 * @throws IOException due to the FXMLLoader
	 * */
	public void loadAboutMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AboutMenu.fxml"));
		DialogPane dialogPane = fxmlLoader.load();

		Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
		aboutAlert.setTitle("About Pain(t)");
		aboutAlert.setDialogPane(dialogPane);
		aboutAlert.show();
	}

	/**
	 * This method loads the HelpMenu FXML file, creates the {@code DialogPane} and shows the Alert
	 * for the Help Menu.
	 *
	 * @throws IOException due to the FXMLLoader
	 * */
	public void loadHelpMenu() throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HelpMenu.fxml"));
		DialogPane dialogPane = fxmlLoader.load();

		Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
		aboutAlert.setTitle("Help with Pain(t)");
		aboutAlert.setDialogPane(dialogPane);
		aboutAlert.show();
	}
}
