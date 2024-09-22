package com.paint.resource;

import com.paint.controller.UtilityController;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public final class AutoSave {
    private UtilityController utilityController;
    private long timerLenInMinutes;

    private ScheduledService<Void> scheduledService;

    public AutoSave() {
        timerLenInMinutes = 10;
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
        scheduledService.setPeriod(Duration.minutes(timerLenInMinutes));
        scheduledService.start();
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
            System.out.println(this.scheduledService.getPeriod());
            this.scheduledService.start();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public double getTimerLenInMinutes() {
        return timerLenInMinutes;
    }

    public void setTimerLenInMinutes(long timerLenInMinutes) {
        // Validate input
        if (timerLenInMinutes > 60 || timerLenInMinutes < 0) {
            return;
        }

        this.timerLenInMinutes = timerLenInMinutes;
        this.scheduledService.setPeriod(Duration.minutes(timerLenInMinutes));
    }

    public UtilityController getUtilityController() {
        return utilityController;
    }

    public void setUtilityController(UtilityController utilityController) {
        this.utilityController = utilityController;
    }
}
