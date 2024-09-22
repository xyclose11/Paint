package com.paint.resource;

import com.paint.controller.UtilityController;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class AutoSave {
    private UtilityController utilityController;
    private long timerLenInSeconds;

    public AutoSave() {
        timerLenInSeconds = 10;
    }

    public void startTimer() {
        ScheduledService<Void> scheduledService = new ScheduledService<Void>() {
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
        scheduledService.setPeriod(Duration.millis(10000));
        scheduledService.start();
    }

    public double getTimerLenInSeconds() {
        return timerLenInSeconds;
    }

    public void setTimerLenInSeconds(long timerLenInSeconds) {
        this.timerLenInSeconds = timerLenInSeconds;
    }

    public UtilityController getUtilityController() {
        return utilityController;
    }

    public void setUtilityController(UtilityController utilityController) {
        this.utilityController = utilityController;
    }
}
