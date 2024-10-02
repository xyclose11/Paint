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

	// Store the workspaces in a hashmap for fast access // TODO HashMap -> LRU Cache + Add a memory monitor option
	public HashMap<Integer, Workspace> getWorkspaceList() {
		return workspaceList;
	}

	public void setWorkspaceList(HashMap<Integer, Workspace> workspaceList) {
		this.workspaceList = workspaceList;
	}

	public Scene getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(Scene currentScene) {
		this.currentScene = currentScene;
	}

	public void addToEndOfWorkspaceList(Workspace workspace) {
		this.workspaceList.put(this.workspaceList.size(), workspace);
		LOGGER.info("New Workspace Added to end of list: {}", workspace);
	}

	public void setCurrentWorkspace(Workspace currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
		LOGGER.info("Current Workspace set to: {}", currentWorkspace);
	}

	public Workspace getCurrentWorkspace() {
		return currentWorkspace;
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
		LOGGER.info("Current File set to: {}", currentFile);
	}

	public TabModel getTabModel() {
		return tabModel;
	}

	public void setTabModel(TabModel tabModel) {
		this.tabModel = tabModel;
	}

	public SettingStateModel getSettingStateModel() {
		return settingStateModel;
	}

	public void setSettingStateModel(SettingStateModel settingStateModel) {
		this.settingStateModel = settingStateModel;
	}

	public PaintStateModel getPaintStateModel() {
		return paintStateModel;
	}

	public void setPaintStateModel(PaintStateModel paintStateModel) {
		this.paintStateModel = paintStateModel;
	}

	public InfoCanvasModel getInfoCanvasModel() {
		return infoCanvasModel;
	}

	public void setInfoCanvasModel(InfoCanvasModel infoCanvasModel) {
		this.infoCanvasModel = infoCanvasModel;
	}

	public int getSize() {
		return this.workspaceList.size();
	}

	public WebServerHandler getWebServerHandler() {
		return webServerHandler;
	}

	public void setWebServerHandler(WebServerHandler webServerHandler) {
		this.webServerHandler = webServerHandler;
	}
}
