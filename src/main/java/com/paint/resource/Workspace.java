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

	/**
	 * Instantiates a new Workspace.
	 *
	 * @param canvasView        the canvas view
	 * @param isActive          the is active
	 * @param paintStateModel   the paint state model
	 * @param infoCanvasModel   the info canvas model
	 * @param settingStateModel the setting state model
	 * @param tabModel          the tab model
	 * @throws IOException the io exception
	 */
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


	/**
	 * Gets undo stack.
	 *
	 * @return the undo stack
	 */
	public Stack<WritableImage> getUndoStack() {
		return this.undoStack;
	}

	/**
	 * Gets redo stack.
	 *
	 * @return the redo stack
	 */
	public Stack<WritableImage> getRedoStack() {
		return this.redoStack;
	}

	/**
	 * Gets canvas view.
	 *
	 * @return the canvas view
	 */
	public HBox getCanvasView() {
		return canvasView;
	}

	/**
	 * Sets canvas view.
	 *
	 * @param canvasView the canvas view
	 */
	public void setCanvasView(HBox canvasView) {
		this.canvasView = canvasView;
	}

	/**
	 * Is active boolean.
	 *
	 * @return the boolean
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Sets active.
	 *
	 * @param active the active
	 */
	public void setActive(boolean active) {
		isActive = active;
	}

	/**
	 * Gets canvas controller.
	 *
	 * @return the canvas controller
	 */
	public CanvasController getCanvasController() {
		return canvasController;
	}

	/**
	 * Sets canvas controller.
	 *
	 * @param canvasController the canvas controller
	 */
	public void setCanvasController(CanvasController canvasController) {
		this.canvasController = canvasController;
	}

	/**
	 * Gets canvas model.
	 *
	 * @return the canvas model
	 */
	public CanvasModel getCanvasModel() {
		return canvasModel;
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
	 * Gets workspace file.
	 *
	 * @return the workspace file
	 */
	public File getWorkspaceFile() {
		return workspaceFile;
	}

	/**
	 * Sets workspace file.
	 *
	 * @param workspaceFile the workspace file
	 */
	public void setWorkspaceFile(File workspaceFile) {
		this.workspaceFile = workspaceFile;
	}
}
