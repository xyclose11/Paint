package com.paint.resource;

import com.paint.controller.UtilityController;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class AutoSave {
    private UtilityController utilityController;
    private Duration timerLenInMinutes;

    private ScheduledService<Void> scheduledService;

    public AutoSave() {
        timerLenInMinutes = Duration.minutes(10);
    }

    public void startTimer() {
        scheduledService = new ScheduledService<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        utilityController.handleFileSave(null);
                        return null;
                    }
                };
            }
        };
        scheduledService.setPeriod(timerLenInMinutes);
        scheduledService.start();
    }

    public void restartAutoSaveService() {
        try {
            this.scheduledService.cancel();
            this.startTimer();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void disableAutoSaveService() {
        try {
            this.scheduledService.cancel();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void enableAutoSaveService() {
        try {
            this.scheduledService.start();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public Duration getTimerLenInMinutes() {
        return timerLenInMinutes;
    }

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
