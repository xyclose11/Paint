package com.paint.resource;

import com.paint.controller.CanvasController;
import com.paint.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;

/*
* Each Workspace requires its own unique CanvasModel & CanvasController
* */
public class Workspace {
	private CanvasModel canvasModel;
	private CanvasController canvasController;
	private boolean isActive = false; // A new workspace is not viewable
	private SceneStateModel sceneStateModel;
	private HBox canvasView;

	public Workspace() throws IOException {
		FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));
		this.canvasView = canvasLoader.load();
		this.canvasModel = new CanvasModel();
		this.canvasController = canvasLoader.getController();
	}

	public Workspace(CanvasModel canvasModel, boolean isActive, PaintStateModel paintStateModel, InfoCanvasModel infoCanvasModel, SettingStateModel settingStateModel, TabModel tabModel) throws IOException {
		FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));
		this.canvasView = canvasLoader.load();
		this.canvasModel = canvasModel;
		this.canvasController = canvasLoader.getController();
		this.isActive = isActive;
		canvasController.setCanvasModel(canvasModel);
//		paintStateModel.setCanvasController(canvasController);
//		paintStateModel.setInfoCanvasModel(infoCanvasModel);
		canvasController.setPaintStateModel(paintStateModel);
		canvasController.setInfoCanvasModel(infoCanvasModel);
		canvasController.setSettingStateModel(settingStateModel);
		canvasController.setSceneStateModel(sceneStateModel);
		canvasController.setTabModel(tabModel);
	}

	public HBox getCanvasView() {
		return  canvasView;
	}

	public void setCanvasView(HBox canvasView) {
		this.canvasView = canvasView;
	}

	public SceneStateModel getSceneStateModel() {
		return sceneStateModel;
	}

	public void setSceneStateModel(SceneStateModel sceneStateModel) {
		this.sceneStateModel = sceneStateModel;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	public void setCanvasController(CanvasController canvasController) {
		this.canvasController = canvasController;
	}

	public CanvasController getCanvasController() {
		return canvasController;
	}

	public CanvasModel getCanvasModel() {
		return canvasModel;
	}
}
