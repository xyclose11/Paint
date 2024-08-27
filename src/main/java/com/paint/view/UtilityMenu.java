package com.paint.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class UtilityMenu {
	private HBox layout;
	private MenuBar menuBar;
	private MenuButton fileSettings;
	private Menu menu;
	private MenuItem open;

	private MenuItem save;
	private MenuItem saveAs;

	public UtilityMenu() {
		layout = new HBox();
		layout.setMinHeight(20);
		layout.setPrefHeight(25);
		open = new MenuItem("Open");
		open.setId("OpenUtilityMenu");
		save = new MenuItem("Save");
		save.setId("SaveUtilityMenu");
		saveAs = new MenuItem("Save As");
		saveAs.setId("SaveAsUtilityMenu");
		menu = new Menu("File");
		menuBar = new MenuBar();
		menu.getItems().addAll(open,save,saveAs);
		menuBar.getMenus().add(menu);
		layout.getChildren().addAll(menuBar);
	}

	public HBox getLayout() {
		return layout;
	}

	public MenuBar getMenuBar() {
		return menuBar;
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

	public void setLayout(HBox layout) {
		this.layout = layout;
	}
}
