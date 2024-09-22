package com.paint.resource;

import com.paint.controller.UtilityController;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

public class AutoSave {
    private UtilityController utilityController;
    private long timerLenInMillis;

    public AutoSave() {
        timerLenInMillis = 10000;
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
        scheduledService.setPeriod(Duration.millis(timerLenInMillis));
        scheduledService.start();
    }

    public double getTimerLenInMillis() {
        return timerLenInMillis;
    }

    public void setTimerLenInMillis(long timerLenInMillis) {
        this.timerLenInMillis = timerLenInMillis;
    }

    public UtilityController getUtilityController() {
        return utilityController;
    }

    public void setUtilityController(UtilityController utilityController) {
        this.utilityController = utilityController;
    }
}
