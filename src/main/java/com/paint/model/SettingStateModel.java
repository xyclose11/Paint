package com.paint.model;

public class SettingStateModel {
    final long INITIAL_AUTOSAVE_INTERVAL = 10;

    private boolean isAutosaveEnabled = true;
    private long autoSaveInterval = INITIAL_AUTOSAVE_INTERVAL;
    private boolean isTimerVisible = true; // Show autoSave timer by default


    public boolean isTimerVisible() {
        return isTimerVisible;
    }

    public void setTimerVisible(boolean timerVisible) {
        isTimerVisible = timerVisible;
    }

    public void setAutoSaveInterval(long autoSaveInterval) {
        this.autoSaveInterval = autoSaveInterval;
    }

    public boolean isAutosaveEnabled() {
        return isAutosaveEnabled;
    }

    public long getAutoSaveInterval() {
        return autoSaveInterval;
    }

    public void setAutosaveEnabled(boolean autosaveEnabled) {
        isAutosaveEnabled = autosaveEnabled;
    }


}
