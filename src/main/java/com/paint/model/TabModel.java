package com.paint.model;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * This model stores the state of Tab related values.
 *
 * @since 1.3
 * */
public class TabModel {
    private TabPane tabPane;
    private Tab newTab;
    private Tab currentTab;

    /**
     * Sets current tab.
     *
     * @param currentTab the current tab
     */
    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }

    /**
     * Gets current tab.
     *
     * @return the current tab
     */
    public Tab getCurrentTab() {
        return currentTab;
    }

    /**
     * Gets tab pane.
     *
     * @return the tab pane
     */
    public TabPane getTabPane() {
        return tabPane;
    }

    /**
     * Sets tab pane.
     *
     * @param tabPane the tab pane
     */
    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    /**
     * Gets new tab.
     *
     * @return the new tab
     */
    public Tab getNewTab() {
        return newTab;
    }

    /**
     * Sets new tab.
     *
     * @param newTab the new tab
     */
    public void setNewTab(Tab newTab) {
        this.newTab = newTab;
    }

    /**
     * Sets tab name.
     *
     * @param tabName the tab name
     */
    public void setTabName(String tabName) {
        // Set the tab name when a file is saved to the file name
        this.currentTab.setText(tabName);
    }

    /**
     * Append unsaved title.
     */
    public void appendUnsavedTitle() {
        if (!this.newTab.getText().contains("*")) {
            this.newTab.setText(this.newTab.getText() + "*");
        }
    }

    /**
     * Handle file saved title.
     */
    public void handleFileSavedTitle() {
        if (this.newTab.getText().contains("*")) {
            String newString = this.newTab.getText().substring(this.newTab.getText().lastIndexOf("*"), this.newTab.getText().length());
            this.newTab.setText(newString);
        }
    }
}
