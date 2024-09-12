package com.paint.model;

import com.paint.resource.Workspace;

import java.io.File;

public class CurrentWorkspaceModel {
	private File currentFile;
	private Workspace currentWorkspace;

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
