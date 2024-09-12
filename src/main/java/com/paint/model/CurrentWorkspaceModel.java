package com.paint.model;

import com.paint.resource.Workspace;

import java.io.File;
import java.util.HashMap;

public class CurrentWorkspaceModel {
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

	public void addToEndOfWorkspaceList(Workspace workspace) {
		this.workspaceList.put(this.workspaceList.size(), workspace);
	}

	public void setCurrentWorkspace(Workspace currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
	}

	public Workspace getCurrentWorkspace() {
		return currentWorkspace;
	}

	public File getCurrentFile() {
		return currentFile;
	}

	public void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}
}
