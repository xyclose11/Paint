package com.paint.resource;

import com.paint.controller.CanvasController;
import com.paint.handler.SelectionHandler;
import com.paint.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * This class represents what the user will interact with. Allows the
 * user to have multiple projects open simultaneously with their own respective
 * undo and redo features.
 * Each Workspace requires its own unique CanvasModel and CanvasController
 *
 * @since 1.4
 * */
public class Workspace {
	private static final Logger LOGGER = LogManager.getLogger();

	private final Stack<WritableImage> undoStack = new Stack<>();
	private final Stack<WritableImage> redoStack = new Stack<>();
	private File workspaceFile; // Used to hold the file representation of the current workspace
	private CanvasModel canvasModel;
	private CanvasController canvasController;
	private boolean isActive = false; // A new workspace is not viewable
	private HBox canvasView;

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
		canvasController.setTabModel(tabModel);
		canvasController.setCurrentWorkspaceModel(paintStateModel.getCurrentWorkspaceModel());

		SelectionHandler selectionHandler = new SelectionHandler();
		selectionHandler.setPaintStateModel(paintStateModel);
		canvasController.setSelectionHandler(selectionHandler);
		this.undoStack.push(canvasController.getCurrentCanvasSnapshot()); // Set initial state as base action for undo
		LOGGER.info("New Workspace Created");
	}

	/**
	 * This method handles the <strong>Redo</strong> functionality
	 *
	 * NOTE: Both <strong>redo</strong> and <strong>undo</strong> function
	 * by taking {canvas snapshots}
	 * */
	public void handleRedoAction() {
		if (this.redoStack.size() >= 1) {
			LOGGER.info("Redo applied");
			WritableImage img = this.redoStack.pop();
			// Re-Add to undo stack
			this.undoStack.push(img);
			getCanvasController().setCanvas(img);
		} else {
			LOGGER.error("Redo Attempted but Redo Stack has current size: {}", this.redoStack.size());
		}
	}

	/**
	 * This method handles the <strong>Undo</strong> functionality
	 *
	 * NOTE: Both <strong>redo</strong> and <strong>undo</strong> function
	 * by taking {canvas snapshots}
	 * */
	public void handleUndoAction() {
		if (this.undoStack.size() >= 1) {
			LOGGER.info("Undo applied");
			WritableImage currentState = this.undoStack.pop(); // Remove last applied change
			getCanvasController().setCanvas(currentState); // Apply prev state to current canvas
			this.redoStack.push(currentState); // Add to redo stack
		} else {
			LOGGER.error("Undo Attempted but Undo Stack has current size: {}", this.undoStack.size());
		}
	}


	public Stack<WritableImage> getUndoStack() {
		return this.undoStack;
	}

	public Stack<WritableImage> getRedoStack() {
		return this.redoStack;
	}

	public HBox getCanvasView() {
		return canvasView;
	}

	public void setCanvasView(HBox canvasView) {
		this.canvasView = canvasView;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public CanvasController getCanvasController() {
		return canvasController;
	}

	public void setCanvasController(CanvasController canvasController) {
		this.canvasController = canvasController;
	}

	public CanvasModel getCanvasModel() {
		return canvasModel;
	}

	public void setCanvasModel(CanvasModel canvasModel) {
		this.canvasModel = canvasModel;
	}

	public File getWorkspaceFile() {
		return workspaceFile;
	}

	public void setWorkspaceFile(File workspaceFile) {
		this.workspaceFile = workspaceFile;
	}
}
