package com.paint.controller;

import com.paint.handler.NotificationsHandler;
import com.paint.handler.WebServerHandler;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.CanvasModel;
import com.paint.model.InfoCanvasModel;
import com.paint.model.PaintStateModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


/**
 * This class is used to control the UI state of the bottom most 'information-bar'
 * @since 1.0
 * */
public class InfoController {
	/**
	 * The Info bar zoom cb.
	 */
	@FXML
	public ComboBox<String> infoBarZoomCB;

	/**
	 * The Info bar zoom slider.
	 */
	@FXML
	public Slider infoBarZoomSlider;

	/**
	 * The Line width lbl.
	 */
	@FXML
    public Label lineWidthLbl;

	/**
	 * The Webserver status label.
	 */
	@FXML
	public Label webserverStatusLabel;

	/**
	 * The Webserver status icon.
	 */
	@FXML
	public FontIcon webserverStatusIcon;

	/**
	 * The Webserver status link.
	 */
	@FXML
	public Hyperlink webserverStatusLink;

	/**
	 * The Notification setting icon.
	 */
	@FXML
	public FontIcon notificationSettingIcon;

	/**
	 * The Notification toggle.
	 */
	@FXML
	public ToggleButton notificationToggle;

	@FXML
	private Label resolutionLbl;


	@FXML
	private Label mousePosLbl;

	private final String CHECKMARK_ICON = "fltfal-checkmark-16";
	private final String DISMISS_ICON = "fltfal-dismiss-16";

	private CanvasModel canvasModel;

	private Group canvasGroup;

	private double currentZoomLvl;

	private InfoCanvasModel infoCanvasModel;

	private PaintStateModel paintStateModel;

	private WorkspaceHandler workspaceHandler;

	private WebServerHandler webServerHandler;

	private NotificationsHandler notificationsHandler;

	/**
	 * Gets web server handler.
	 *
	 * @return the web server handler
	 */
	public WebServerHandler getWebServerHandler() {
		return webServerHandler;
	}

	/**
	 * Sets web server handler.
	 *
	 * @param webServerHandler the web server handler
	 */
	public void setWebServerHandler(WebServerHandler webServerHandler) {
		this.webServerHandler = webServerHandler;
	}

	/**
	 * Gets current workspace model.
	 *
	 * @return the current workspace model
	 */
	public WorkspaceHandler getCurrentWorkspaceModel() {
		return workspaceHandler;
	}

	/**
	 * Sets current workspace model.
	 *
	 * @param workspaceHandler the workspace handler
	 */
	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	/**
	 * Gets info bar zoom cb.
	 *
	 * @return the info bar zoom cb
	 */
	public ComboBox<String> getInfoBarZoomCB() {
		return infoBarZoomCB;
	}

	/**
	 * Sets info bar zoom cb.
	 *
	 * @param infoBarZoomCB the info bar zoom cb
	 */
	public void setInfoBarZoomCB(ComboBox<String> infoBarZoomCB) {
		this.infoBarZoomCB = infoBarZoomCB;
	}

	/**
	 * Gets paint state model.
	 *
	 * @return the paint state model
	 */
	public PaintStateModel getPaintStateModel() {
		return paintStateModel;
	}

	/**
	 * Sets paint state model.
	 *
	 * @param paintStateModel the paint state model
	 */
	public void setPaintStateModel(PaintStateModel paintStateModel) {
		this.paintStateModel = paintStateModel;
	}

	/**
	 * Sets info canvas model.
	 *
	 * @param infoCanvasModel the info canvas model
	 */
	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;

		// Bind text props for resolution, mouse POS, & selection resolution
		this.mousePosLbl.textProperty().bind(this.infoCanvasModel.getMousePosLbl().textProperty());
		this.resolutionLbl.textProperty().bind(this.infoCanvasModel.getResolutionLbl().textProperty());
		this.lineWidthLbl.textProperty().bind(this.infoCanvasModel.getCurrentLineWidthLbl().textProperty());
	}

	/**
	 * Sets canvas model.
	 *
	 * @param canvasModel the canvas model
	 */
	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
		this.canvasGroup = canvasModel.getCanvasGroup();
	}

	@FXML
	private void onZoomCBChange(ActionEvent actionEvent) {
		// Set zoom slider to CB zoom value

		// Convert string -> double
		try {
			String cbVal = this.infoBarZoomCB.getValue();

			// Check if there is a '%' symbol
			if (cbVal.lastIndexOf("%") != -1) { // Eval -> true means that there is a '%' symbol
				cbVal = cbVal.substring(0, cbVal.length() - 1); // Remove '%' symbol
			}

			double sliderVal = Double.parseDouble(cbVal); // Convert string -> double to set slider POS
			this.infoBarZoomSlider.setValue(sliderVal);

			this.workspaceHandler.getCurrentWorkspace().getCanvasModel().setZoomScale(sliderVal / 100.0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		handleZoom();
	}

	@FXML
	private void onZoomSliderChange(MouseEvent mouseEvent) {
		// Set zoom combo box to zoom value
		this.infoBarZoomCB.setValue(String.valueOf(this.infoBarZoomSlider.getValue()));
		this.workspaceHandler.getCurrentWorkspace().getCanvasModel().setZoomScale(infoBarZoomSlider.getValue() / 100.0);
		handleZoom();
	}

	// Handle scaling/zoom
	private void handleZoom() {
		canvasGroup = this.workspaceHandler.getCurrentWorkspace().getCanvasModel().getCanvasGroup();
		double newZoomScale = this.workspaceHandler.getCurrentWorkspace().getCanvasModel().getZoomScale(); // Divide by 100.0 to get proper format for setScaleX/Y
		canvasGroup.setScaleX(newZoomScale);
		canvasGroup.setScaleY(newZoomScale);
	}

	private void handleLinkMouseClick(MouseEvent mouseEvent) {
		try {
			Desktop.getDesktop().browse(new URL(this.workspaceHandler.getWebServerHandler().getDefaultServerURL()).toURI());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Desktop error: " + e.getMessage());
		} catch (URISyntaxException e){
			System.out.println(e.getMessage());
			throw new RuntimeException("Desktop URI error: " + e.getMessage());
		}
	}

	private void handleNotificationToggleClick(MouseEvent mouseEvent) {
		this.notificationsHandler.setNotificationsActive(this.notificationToggle.isSelected());

		if (this.notificationToggle.isSelected()) {
			this.notificationSettingIcon.setIconLiteral(CHECKMARK_ICON);
		} else {
			this.notificationSettingIcon.setIconLiteral(DISMISS_ICON);
		}
	}

	/**
	 * Initialize.
	 */
	@FXML
	public void initialize() {
		// setup webServer hyperlink listeners
		this.webserverStatusLink.setOnMouseClicked(this::handleLinkMouseClick);

		this.notificationToggle.setOnMouseClicked(this::handleNotificationToggleClick);
	}

	/**
	 * Gets webserver status label.
	 *
	 * @return the webserver status label
	 */
	public Label getWebserverStatusLabel() {
		return webserverStatusLabel;
	}

	/**
	 * Sets webserver status label.
	 *
	 * @param webserverStatusLabel the webserver status label
	 */
	public void setWebserverStatusLabel(Label webserverStatusLabel) {
		this.webserverStatusLabel = webserverStatusLabel;
	}

	/**
	 * Gets webserver status link.
	 *
	 * @return the webserver status link
	 */
	public Hyperlink getWebserverStatusLink() {
		return webserverStatusLink;
	}

	/**
	 * Sets webserver status link.
	 *
	 * @param webserverStatusLink the webserver status link
	 */
	public void setWebserverStatusLink(Hyperlink webserverStatusLink) {
		this.webserverStatusLink = webserverStatusLink;
	}

	/**
	 * Gets webserver status icon.
	 *
	 * @return the webserver status icon
	 */
	public FontIcon getWebserverStatusIcon() {
		return webserverStatusIcon;
	}

	/**
	 * Sets webserver status icon.
	 *
	 * @param webserverStatusIcon the webserver status icon
	 */
	public void setWebserverStatusIcon(FontIcon webserverStatusIcon) {
		this.webserverStatusIcon = webserverStatusIcon;
	}

	/**
	 * Gets notifications handler.
	 *
	 * @return the notifications handler
	 */
	public NotificationsHandler getNotificationsHandler() {
		return notificationsHandler;
	}

	/**
	 * Sets notifications handler.
	 *
	 * @param notificationsHandler the notifications handler
	 */
	public void setNotificationsHandler(NotificationsHandler notificationsHandler) {
		this.notificationsHandler = notificationsHandler;
	}
}
