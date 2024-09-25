package com.paint.resource;

import com.paint.controller.UtilityController;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

/**
 * AutoSave utilizes Threading alongside JavaFX ScheduledService, and is responsible
 * for instantiating file auto-save throughout the application.
 * */
public class AutoSave {
    private UtilityController utilityController;
    private Duration timerLenInMinutes;

    private ScheduledService<Void> scheduledService;

    public AutoSave() {
        timerLenInMinutes = Duration.minutes(10);
    }

    /**
     * This method instantiates a new ScheduledService<Void> object, which creates a new task called 'createTask'.
     * This task is responsible for invoking the method from the UtilityController to save the current working file.
     *
     * */
    public void startTimer() {
        scheduledService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        utilityController.startAutoSaveTimer();
                        utilityController.handleFileSave(null);
                        return null;
                    }
                };
            }
        };
        scheduledService.setPeriod(timerLenInMinutes);
        scheduledService.start();
    }

    /**
     * This method cancels the current ScheduledService and instantiates a new
     * ScheduledService to restart the AutoSave timer. An example would be, to
     * reset the timer when a user manually saves.
     * */
    public void restartAutoSaveService() {
        try {
            this.scheduledService.cancel();
            this.startTimer();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * This method cancels the ScheduledService which in turn disables the auto-save feature.
     * */
    public void disableAutoSaveService() {
        try {
            this.scheduledService.cancel();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    /**
     * This method starts the ScheduledService which in turn enables the auto-save feature.
     * */
    public void enableAutoSaveService() {
        try {
            this.scheduledService.start();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Returns the overall length of the timer.
     * NOTE: This does NOT return the time remaining.
     * @return Duration
     * */
    public Duration getTimerLenInMinutes() {
        return timerLenInMinutes;
    }

    /**
     * This method updates the overall length of the timer in minutes.
     *
     * NOTE: This method takes affect IMMEDIATELY, meaning it will cancel the current remaining time left in the current timer
     * and start a new ScheduledService instance with the new time length.
     *
     * @param timerLenInMinutes value in minutes for how long the auto-save timer should be.
     * */
    public void setTimerLenInMinutes(long timerLenInMinutes) {
        // Validate input
        if (timerLenInMinutes > 60 || timerLenInMinutes < 0) {
            return;
        }

        this.timerLenInMinutes = Duration.minutes(timerLenInMinutes);
        this.scheduledService.cancel();
        startTimer();
    }

    public UtilityController getUtilityController() {
        return utilityController;
    }

    public void setUtilityController(UtilityController utilityController) {
        this.utilityController = utilityController;
    }
}
