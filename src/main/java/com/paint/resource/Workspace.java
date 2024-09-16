package com.paint.resource;

import com.paint.controller.CanvasController;
import com.paint.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

/*
* Each Workspace requires its own unique CanvasModel & CanvasController
* */
public class Workspace {
	private File workspaceFile; // Used to hold the file representation of the current workspace
	private CanvasModel canvasModel;
	private CanvasController canvasController;
	private boolean isActive = false; // A new workspace is not viewable
	private SceneStateModel sceneStateModel;
	private HBox canvasView;
	private Stack<WritableImage> undoStack = new Stack<>();

	public Stack<WritableImage> getUndoStack(){ return this.undoStack; }

	private Stack<WritableImage> redoStack = new Stack<>();

	public Stack<WritableImage> getRedoStack(){ return this.redoStack; }

	public Workspace(HBox canvasView, boolean isActive, PaintStateModel paintStateModel, InfoCanvasModel infoCanvasModel, SettingStateModel settingStateModel, TabModel tabModel) throws IOException {
		FXMLLoader canvasLoader = new FXMLLoader(getClass().getResource("/view/CanvasView.fxml"));
		this.canvasView = canvasLoader.load();
		this.canvasModel = new CanvasModel();

		this.canvasController = canvasLoader.getController();
		this.isActive = isActive;

		canvasModel.setCanvasView(canvasView);

		canvasController.setCanvasModel(canvasModel);
		canvasController.setPaintStateModel(paintStateModel);
		canvasController.setInfoCanvasModel(infoCanvasModel);
		canvasController.setSettingStateModel(settingStateModel);
		canvasController.setSceneStateModel(sceneStateModel);
		canvasController.setTabModel(tabModel);
		canvasController.setCurrentWorkspaceModel(paintStateModel.getCurrentWorkspaceModel());

		this.undoStack.push(canvasController.getCurrentCanvasSnapshot()); // Set initial state as base action for undo
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

	public void setWorkspaceFile(File workspaceFile) {
		this.workspaceFile = workspaceFile;
	}

	public File getWorkspaceFile() {
		return workspaceFile;
	}
}
