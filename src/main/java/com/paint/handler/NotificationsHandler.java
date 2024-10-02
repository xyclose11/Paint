package com.paint.handler;

import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

public final class NotificationsHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    // track whether the user wants notifications enabled
    private boolean isFileOpenNT = true;
    private boolean isFileSaveNT = true;
    private boolean isFileHostedNT = true;

    public void showFileOpenedNotification(String fileName, Window mainWindow) {
        if (!isFileOpenNT) {
            LOGGER.info("Attempted to show file opened notification, but is disabled");
            return; // file open notification disabled
        }

        Notifications.create()
                .title("File " + fileName + " has been opened.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();

        LOGGER.info("File {} Opened Notification", fileName);
    }

    public void showFileSavedNotification(String fileName, Window mainWindow) {
        if (!isFileSaveNT) {
            LOGGER.info("Attempted to show file saved notification, but is disabled");
            return;
        }

        Notifications.create()
                .title("File " + fileName + " has been saved.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
        LOGGER.info("File {} Saved Notification", fileName);

    }
}
