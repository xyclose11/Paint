package com.paint.controller;

import com.paint.model.*;
import com.paint.resource.Workspace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class TabController {
	private TabModel tabModel;
	private CanvasModel canvasModel;
	private PaintStateModel paintStateModel;
	private InfoCanvasModel infoCanvasModel;
	private SettingStateModel settingStateModel;
	private CurrentWorkspaceModel currentWorkspaceModel;

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
		TabPane tabPane = this.tabModel.getTabPane();
		Tab newTab = new Tab("New File");

		newTab.setOnCloseRequest(closeEvent -> {
			this.currentWorkspaceModel.getWorkspaceList().remove(tabPane.getTabs().size() - 1);
		});


		Workspace workspace = new Workspace(canvasModel, true, paintStateModel, infoCanvasModel, settingStateModel, tabModel);

		// Set the current workspace in focus
		this.currentWorkspaceModel.setCurrentWorkspace(workspace);

		// Add to total workspace list
		this.currentWorkspaceModel.addToEndOfWorkspaceList(workspace);

		newTab.setContent(workspace.getCanvasView());

		int addNewFileTabIndex= tabPane.getTabs().indexOf(addNewFileTab);

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
	}
	// TAB Handler SECTION END


	public void setCurrentWorkspaceModel(CurrentWorkspaceModel currentWorkspaceModel) {
		this.currentWorkspaceModel = currentWorkspaceModel;
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
}
