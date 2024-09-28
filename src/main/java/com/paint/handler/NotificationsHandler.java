package com.paint.handler;

import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public final class NotificationsHandler {
    private Notifications notifications;

    // track whether the user wants notifications enabled
    private boolean isFileOpenNT = true;
    private boolean isFileSaveNT = true;
    private boolean isFileHostedNT = true;

    public void showFileOpenedNotification(String fileName, Window mainWindow) {
        if (!isFileOpenNT) {
            return; // file open notification disabled
        }

        Notifications.create()
                .title("File " + fileName + " has been opened.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }

    public void showFileSavedNotification(String fileName, Window mainWindow) {
        if (!isFileSaveNT) {
            return;
        }

        Notifications.create()
                .title("File " + fileName + " has been saved.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }

    public void showFileHostedNotification(String fileName, Window mainWindow, String webAddr) {
        if (!isFileHostedNT) {
            return;
        }

        Notifications.create()
                .title("File " + fileName + " is currently being hosted at: " + webAddr)
                .hideAfter(new Duration(6000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }
}
