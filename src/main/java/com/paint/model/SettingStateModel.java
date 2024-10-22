package com.paint.model;

import com.paint.resource.AutoSave;

/**
 * This model stores the state of setting related values
 *
 * @deprecated This model is currently being phased out due to a lack of applicable settings that would not fall under another model
 * @since 1.2
 * */
public class SettingStateModel {
    /**
     * The Initial autosave interval.
     */
    final long INITIAL_AUTOSAVE_INTERVAL = 10; // 10 minutes

    private boolean isAutosaveEnabled = true;
    private long autoSaveInterval = INITIAL_AUTOSAVE_INTERVAL;
    private boolean isTimerVisible = true; // Show autoSave timer by default

    private AutoSave autoSave;

    /**
     * Gets auto save.
     *
     * @return the auto save
     */
    public AutoSave getAutoSave() {
        return autoSave;
    }

    /**
     * Sets auto save.
     *
     * @param autoSave the auto save
     */
    public void setAutoSave(AutoSave autoSave) {
        this.autoSave = autoSave;
    }

    /**
     * Is timer visible boolean.
     *
     * @return the boolean
     */
    public boolean isTimerVisible() {
        return isTimerVisible;
    }

    /**
     * Sets timer visible.
     *
     * @param timerVisible the timer visible
     */
    public void setTimerVisible(boolean timerVisible) {
        isTimerVisible = timerVisible;
    }

    /**
     * Sets auto save interval.
     *
     * @param autoSaveInterval the auto save interval
     */
    public void setAutoSaveInterval(long autoSaveInterval) {
        // Check if interval has changed
        if (this.autoSaveInterval != autoSaveInterval) {
            this.autoSaveInterval = autoSaveInterval;
            autoSave.setTimerLenInMinutes(autoSaveInterval);
        }
    }

    /**
     * Is autosave enabled boolean.
     *
     * @return the boolean
     */
    public boolean isAutosaveEnabled() {
        return isAutosaveEnabled;
    }

    /**
     * Gets auto save interval.
     *
     * @return the auto save interval
     */
    public long getAutoSaveInterval() {
        return autoSaveInterval;
    }

    /**
     * Sets autosave enabled.
     *
     * @param autosaveEnabled the autosave enabled
     */
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
