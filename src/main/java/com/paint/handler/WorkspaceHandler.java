package com.paint.handler;

import com.paint.model.InfoCanvasModel;
import com.paint.model.PaintStateModel;
import com.paint.model.SettingStateModel;
import com.paint.model.TabModel;
import com.paint.resource.Workspace;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.HashMap;

/**
 * This class is responsible for Handling all the Workspace instances in the application.
 *
 * This is primarily used for the 'Tab' feature
 *
 * NOTE: All workspaces are held within the HashMap data structure for quick lookup
 *
 *
 * @since 1.3
 * */
public class WorkspaceHandler {
	private static final Logger LOGGER = LogManager.getLogger();

	// Store models for new file creation
	private TabModel tabModel;
	private SettingStateModel settingStateModel;
	private PaintStateModel paintStateModel;
	private InfoCanvasModel infoCanvasModel;
	private Scene currentScene; // Merged from SceneStateModel
	private WebServerHandler webServerHandler;

	private File currentFile;
	private Workspace currentWorkspace;
	private HashMap<Integer, Workspace> workspaceList = new HashMap<>();

	/**
	 * Gets workspace list.
	 *
	 * @return the workspace list
	 */
// Store the workspaces in a hashmap for fast access
	public HashMap<Integer, Workspace> getWorkspaceList() {
		return workspaceList;
	}

	/**
	 * Sets workspace list.
	 *
	 * @param workspaceList the workspace list
	 */
	public void setWorkspaceList(HashMap<Integer, Workspace> workspaceList) {
		this.workspaceList = workspaceList;
	}

	/**
	 * Gets current scene.
	 *
	 * @return the current scene
	 */
	public Scene getCurrentScene() {
		return currentScene;
	}

	/**
	 * Sets current scene.
	 *
	 * @param currentScene the current scene
	 */
	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
	}

	/**
	 * Add to end of workspace list.
	 *
	 * @param workspace the workspace
	 */
	public void addToEndOfWorkspaceList(Workspace workspace) {
		this.workspaceList.put(this.workspaceList.size(), workspace);
		LOGGER.info("New Workspace Added to end of list: {}", workspace);
	}

	/**
	 * Sets current workspace.
	 *
	 * @param currentWorkspace the current workspace
	 */
	public void setCurrentWorkspace(Workspace currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
		LOGGER.info("Current Workspace set to: {}", currentWorkspace);
	}

	/**
	 * Gets current workspace.
	 *
	 * @return the current workspace
	 */
	public Workspace getCurrentWorkspace() {
		return currentWorkspace;
	}

	/**
	 * Gets current file.
	 *
	 * @return the current file
	 */
	public File getCurrentFile() {
		return currentFile;
	}

	/**
	 * Sets current file.
	 *
	 * @param currentFile the current file
	 */
	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
		LOGGER.info("Current File set to: {}", currentFile);
	}

	/**
	 * Gets tab model.
	 *
	 * @return the tab model
	 */
	public TabModel getTabModel() {
		return tabModel;
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
	 * Gets setting state model.
	 *
	 * @return the setting state model
	 */
	public SettingStateModel getSettingStateModel() {
		return settingStateModel;
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
	 * Gets size.
	 *
	 * @return the size
	 */
	public int getSize() {
		return this.workspaceList.size();
	}

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
