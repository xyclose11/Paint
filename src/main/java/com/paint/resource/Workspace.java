package com.paint.resource;

import com.paint.controller.CanvasController;
import com.paint.model.CanvasModel;
import com.paint.model.SceneStateModel;
import javafx.scene.layout.HBox;

/*
* Each Workspace requires its own unique CanvasModel & CanvasController
* */
public class Workspace {
	private CanvasModel canvasModel;
	private CanvasController canvasController;
	private boolean isActive = false; // A new workspace is not viewable
	private SceneStateModel sceneStateModel;
	private HBox canvasView;

	public Workspace() {
		this.canvasModel = new CanvasModel();
		this.canvasController = new CanvasController();
	}

	public Workspace(CanvasModel canvasModel, CanvasController canvasController, boolean isActive) {
		this.canvasModel = canvasModel;
		this.canvasController = canvasController;
		this.isActive = isActive;
	}

	public HBox getCanvasView() {
		return canvasView;
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
