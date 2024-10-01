package com.paint.handler;

import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public final class NotificationsHandler {
    private Notifications notifications;

    // track whether the user wants notifications enabled
    private boolean notificationsActive = true;


    public void showFileOpenedNotification(String fileName, Window mainWindow) {
        if (!notificationsActive) {
            return; // file open notification disabled
        }

        Notifications.create()
                .title("File " + fileName + " has been opened.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }

    public void showFileSavedNotification(String fileName, Window mainWindow) {
        if (!notificationsActive) {
            return;
        }

        Notifications.create()
                .title("File " + fileName + " has been saved.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }

    public void showFileHostedNotification(String fileName, Window mainWindow, String webAddr) {
        if (!notificationsActive) {
            return;
        }

        Notifications.create()
                .title("File " + fileName + " is currently being hosted at: " + webAddr)
                .hideAfter(new Duration(6000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }

    public boolean isNotificationsActive() {
        return notificationsActive;
    }

    public void setNotificationsActive(boolean notificationsActive) {
        this.notificationsActive = notificationsActive;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }
}
