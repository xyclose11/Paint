package com.paint.handler;

import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public final class NotificationsHandler {
    private Notifications notifications;

    // track whether the user wants notifications enabled
    private boolean fileOpenNTEnabled = true;
    private boolean fileSaveNTEnabled = true;
    private boolean fileHostedNTEnabled = true;

    public void showFileOpenedNotification(String fileName, Window mainWindow) {
        Notifications.create()
                .title("File " + fileName + " has been opened.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }

    public void showFileSavedNotification() {

    }

    public void showFileHostedNotification() {

    }
}
