package com.paint.model;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;

public class SettingStateModel {
    private CheckBox autosaveCB;
    private Slider autosaveIntervalSlider;
    private boolean isAutosaveEnabled;
    private double autoSaveInterval;

    public boolean isAutosaveEnabled() {
        return isAutosaveEnabled;
    }

    public double getAutoSaveInterval() {
        return autoSaveInterval;
    }

    public void setAutosaveEnabled(boolean autosaveEnabled) {
        isAutosaveEnabled = autosaveEnabled;
    }

    public void setAutoSaveIntervalVal(double autoSaveInterval) {
        this.autoSaveInterval = autoSaveInterval;
    }

    public CheckBox getAutosaveCB() {
        return autosaveCB;
    }

    public Slider getAutosaveIntervalSlider() {
        return autosaveIntervalSlider;
    }

    public void setAutosaveCB(CheckBox autosaveCB) {
        this.autosaveCB = autosaveCB;
    }

    public void setAutosaveIntervalSlider(Slider autosaveIntervalSlider) {
        this.autosaveIntervalSlider = autosaveIntervalSlider;
    }
}
