package com.paint.resource;

import com.paint.controller.UtilityController;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class AutoSave {
    private final ScheduledExecutorService autoSaveScheduler = Executors.newScheduledThreadPool(2);
    private UtilityController utilityController;
    private long timerLenInSeconds;

    public AutoSave() {
        timerLenInSeconds = 10;
    }

    public void beepForAnHour() {
        Runnable beeper = () -> System.out.println("beep");
        ScheduledFuture<?> beeperHandle =
                autoSaveScheduler.scheduleAtFixedRate(beeper, 5, 10, SECONDS);
//        Runnable canceller = () -> beeperHandle.cancel(false);
//        autoSaveScheduler.schedule(canceller, 1, HOURS);
    }

    public void startTimer() {
        Runnable timer = () -> {
            System.out.println("TRYING");
            try {
                System.out.println("SAVING");
                this.utilityController.handleFileSave(null);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
        autoSaveScheduler.scheduleAtFixedRate(timer, 15, 10, SECONDS);
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
