package com.paint.model;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabModel {
    private TabPane tabPane;
    private Tab newTab;
    private Tab currentTab;

    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }

    public Tab getCurrentTab() {
        return currentTab;
    }

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

    public void setTabName(String tabName) {
        // Set the tab name when a file is saved to the file name
        this.currentTab.setText(tabName);
    }

    public void appendUnsavedTitle() {
        if (!this.newTab.getText().contains("*")) {
            this.newTab.setText(this.newTab.getText() + "*");
        }
    }

    public void handleFileSavedTitle() {
        if (this.newTab.getText().contains("*")) {
            String newString = this.newTab.getText().substring(this.newTab.getText().lastIndexOf("*"), this.newTab.getText().length());
            this.newTab.setText(newString);
        }
    }
}
