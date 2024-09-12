package com.paint.model;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabModel {
    private TabPane tabPane;
    private Tab newTab;

    public TabPane getTabPane() {
        return tabPane;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public Tab getNewTab() {
        return newTab;
    }

    public void setNewTab(Tab newTab) {
        this.newTab = newTab;
    }
}
