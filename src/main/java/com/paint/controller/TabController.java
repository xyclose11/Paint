package com.paint.controller;

import com.paint.handler.WebServerHandler;
import com.paint.handler.WorkspaceHandler;
import com.paint.model.*;
import com.paint.resource.Workspace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 *
 * This class is responsible for controlling the UI related to 'Tabs'
 * @since 1.4
 * */
public class TabController {
	private TabModel tabModel;
	private CanvasModel canvasModel;
	private PaintStateModel paintStateModel;
	private InfoCanvasModel infoCanvasModel;
	private SettingStateModel settingStateModel;
	private WorkspaceHandler workspaceHandler;
	private WebServerHandler webServerHandler;
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The File tab pane.
	 */
	@FXML
	public TabPane fileTabPane;

	/**
	 * The Add new file tab.
	 */
	@FXML
	public Tab addNewFileTab;

	/**
	 * The Current file tab.
	 */
	@FXML
	public Tab currentFileTab;

	/**
	 * The Add new file tab btn.
	 */
	@FXML
	public Button addNewFileTabBtn;



	// TAB Handler SECTION START
	@FXML
	private void onMouseClickedNewFileTab(MouseEvent event) throws IOException {
		createNewTab();
		LOGGER.info("Mouse Click New File Tab: X:{}Y:{}", event.getX(), event.getY());
	}

	/**
	 * On key pressed new file tab.
	 *
	 * @param keyEvent the key event
	 * @throws IOException the io exception
	 */
	public void onKeyPressedNewFileTab(KeyEvent keyEvent) throws IOException {
		createNewTab();
	}

	/**
	 * This method creates a new tab, adds it to the tabPane, and sets applicable event handlers
	 *
	 * @throws IOException
	 * */
	public void createNewTab() throws IOException {
		TabPane tabPane = this.tabModel.getTabPane();
		Tab newTab = new Tab("New File");

		newTab.setOnCloseRequest(closeEvent -> {
			this.workspaceHandler.getWorkspaceList().remove(tabPane.getTabs().size() - 1);
			// on tab close exit current node from transformation mode
			if (this.workspaceHandler.getPaintStateModel().getCurrentNode() != null) {
				this.workspaceHandler.getPaintStateModel().getCurrentNode().exitTransformMode();
			}
		});

		HBox canvasView = this.canvasModel.getCanvasView();

		Workspace workspace = new Workspace(canvasView, true, paintStateModel, infoCanvasModel, settingStateModel, tabModel);
		// Set the current workspace in focus
		this.workspaceHandler.setCurrentWorkspace(workspace);

		// Add to total workspace list
		this.workspaceHandler.addToEndOfWorkspaceList(workspace);

		newTab.setContent(workspace.getCanvasView());

		int addNewFileTabIndex = tabPane.getTabs().indexOf(addNewFileTab);

		tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);

		// Check if there are any tabs to remove
		if (tabPane.getTabs().size() <= 1) {
			this.tabModel.setCurrentTab(newTab);
		}

		tabPane.getTabs().add(newTab);
		tabPane.getTabs().remove(addNewFileTabIndex);
		tabPane.getTabs().add(addNewFileTab);

		// Set focus on newly created tab
		tabPane.getSelectionModel().select(newTab);

		this.tabModel.setNewTab(newTab);

		this.webServerHandler.updateCurrentFile(null);

		LOGGER.info("New Tab Created: {} | At index: {}", newTab, tabPane.getTabs().size());
	}
	// TAB Handler SECTION END


	/**
	 * Sets current workspace model.
	 *
	 * @param workspaceHandler the workspace handler
	 */
	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	/**
	 * Sets tab model.
	 *
	 * @param tabModel the tab model
	 */
	public void setTabModel(TabModel tabModel) {
		this.tabModel = tabModel;
	}

	/**
	 * Sets canvas model.
	 *
	 * @param canvasModel the canvas model
	 */
	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	/**
	 * Gets canvas model.
	 *
	 * @return the canvas model
	 */
	public CanvasModel getCanvasModel() {
		return this.canvasModel;
	}

	/**
	 * Gets tab model.
	 *
	 * @return the tab model
	 */
	public TabModel getTabModel() {
		return this.tabModel;
	}

	/**
	 * Gets info canvas model.
	 *
	 * @return the info canvas model
	 */
	public InfoCanvasModel getInfoCanvasModel() {
		return infoCanvasModel;
	}

	/**
	 * Sets info canvas model.
	 *
	 * @param infoCanvasModel the info canvas model
	 */
	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;
	}

	/**
	 * Sets setting state model.
	 *
	 * @param settingStateModel the setting state model
	 */
	public void setSettingStateModel(SettingStateModel settingStateModel) {
		this.settingStateModel = settingStateModel;
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
	 * Gets add new file tab btn.
	 *
	 * @return the add new file tab btn
	 */
	public Button getAddNewFileTabBtn() { return this.addNewFileTabBtn; }

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
}
