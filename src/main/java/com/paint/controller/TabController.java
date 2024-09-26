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

	@FXML
	public TabPane fileTabPane;

	@FXML
	public Tab addNewFileTab;

	@FXML
	public Tab currentFileTab;

	@FXML
	public Button addNewFileTabBtn;



	// TAB Handler SECTION START
	@FXML
	private void onMouseClickedNewFileTab(MouseEvent event) throws IOException {
		createNewTab();
	}

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

		// TODO split this up into multiple methods i.e. 1.) Create new Tab 2. set events 3. tabPane stuff etc.

		newTab.setOnCloseRequest(closeEvent -> {
			this.workspaceHandler.getWorkspaceList().remove(tabPane.getTabs().size() - 1);
			// on tab close exit current node from transformation mode
			this.workspaceHandler.getPaintStateModel().getCurrentShape().exitTransformMode();
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
	}
	// TAB Handler SECTION END


	public void setCurrentWorkspaceModel(WorkspaceHandler workspaceHandler) {
		this.workspaceHandler = workspaceHandler;
	}

	public void setTabModel(TabModel tabModel) {
		this.tabModel = tabModel;
	}

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	public CanvasModel getCanvasModel() {
		return this.canvasModel;
	}

	public TabModel getTabModel() {
		return this.tabModel;
	}

	public InfoCanvasModel getInfoCanvasModel() {
		return infoCanvasModel;
	}

	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;
	}

	public void setSettingStateModel(SettingStateModel settingStateModel) {
		this.settingStateModel = settingStateModel;
	}

	public void setPaintStateModel(PaintStateModel paintStateModel) {
		this.paintStateModel = paintStateModel;
	}

	public Button getAddNewFileTabBtn() { return this.addNewFileTabBtn; }

	public WebServerHandler getWebServerHandler() {
		return webServerHandler;
	}

	public void setWebServerHandler(WebServerHandler webServerHandler) {
		this.webServerHandler = webServerHandler;
	}
}
