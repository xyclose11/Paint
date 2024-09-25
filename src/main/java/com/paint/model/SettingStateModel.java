package com.paint.model;

import com.paint.resource.AutoSave;

public class SettingStateModel {
    final long INITIAL_AUTOSAVE_INTERVAL = 10; // 10 minutes

    private boolean isAutosaveEnabled = true;
    private long autoSaveInterval = INITIAL_AUTOSAVE_INTERVAL;
    private boolean isTimerVisible = true; // Show autoSave timer by default

    private AutoSave autoSave;

    public AutoSave getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(AutoSave autoSave) {
        this.autoSave = autoSave;
    }

    public boolean isTimerVisible() {
        return isTimerVisible;
    }

    public void setTimerVisible(boolean timerVisible) {
        isTimerVisible = timerVisible;
    }

    public void setAutoSaveInterval(long autoSaveInterval) {
        // Check if interval has changed
        if (this.autoSaveInterval != autoSaveInterval) {
            this.autoSaveInterval = autoSaveInterval;
            autoSave.setTimerLenInMinutes(autoSaveInterval);
        }
    }

    public boolean isAutosaveEnabled() {
        return isAutosaveEnabled;
    }

    public long getAutoSaveInterval() {
        return autoSaveInterval;
    }

    public void setAutosaveEnabled(boolean autosaveEnabled) {
        if (this.isAutosaveEnabled && autosaveEnabled) { // Check if autoSave was previously enabled and new val is true
            return; // do nothing to avoid duplicate tasks
        }

        if (autosaveEnabled) {
            autoSave.enableAutoSaveService();
        } else {
            autoSave.disableAutoSaveService();
        }
        isAutosaveEnabled = autosaveEnabled;
    }


}
