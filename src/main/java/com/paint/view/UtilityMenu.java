package com.paint.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class UtilityMenu {
	private HBox rootContainer;
	private MenuBar menuBar;
	private MenuButton fileSettings;
	private Menu menu;
	private MenuItem open;

	private MenuItem save;
	private MenuItem saveAs;

	public UtilityMenu() {
		rootContainer = new HBox();
		open = new MenuItem("Open");
		save = new MenuItem("Save");
		saveAs = new MenuItem("Save As");
		menu = new Menu("File");
		menuBar = new MenuBar();
	}

	public HBox Init() {
		menu.getItems().addAll(open,save,saveAs);
		menuBar.getMenus().add(menu);
		rootContainer.getChildren().addAll(menuBar);
		return rootContainer;
	}

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public HBox getRootContainer() {
		return rootContainer;
	}

	public MenuButton getFileSettings() {
		return fileSettings;
	}

	public void setFileSettings(MenuButton fileSettings) {
		this.fileSettings = fileSettings;
	}

	public void setMenuBar(MenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public void setRootContainer(HBox rootContainer) {
		this.rootContainer = rootContainer;
	}
}
