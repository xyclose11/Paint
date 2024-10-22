package com.paint.handler;

import javafx.stage.Window;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

/**
 * The type Notifications handler.
 */
public final class NotificationsHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    // track whether the user wants notifications enabled
    private boolean notificationsActive = true;


    /**
     * Show file opened notification.
     *
     * @param fileName   the file name
     * @param mainWindow the main window
     */
    public void showFileOpenedNotification(String fileName, Window mainWindow) {
        if (!notificationsActive) {
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

    /**
     * Show file saved notification.
     *
     * @param fileName   the file name
     * @param mainWindow the main window
     */
    public void showFileSavedNotification(String fileName, Window mainWindow) {
        if (!notificationsActive) {
	        LOGGER.info("Attempted to show file saved notification, but is disabled");
            return;
        }

        Notifications.create()
                .title("File " + fileName + " has been saved.")
                .hideAfter(new Duration(2000))
                .owner(mainWindow) // used so that it shows in bottom of application not the screen
                .showInformation();
    }

    /**
     * Show file hosted notification.
     *
     * @param fileName   the file name
     * @param mainWindow the main window
     * @param webAddr    the web addr
     */
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

    /**
     * Is notifications active boolean.
     *
     * @return the boolean
     */
    public boolean isNotificationsActive() {
        return notificationsActive;
    }

    /**
     * Sets notifications active.
     *
     * @param notificationsActive the notifications active
     */
    public void setNotificationsActive(boolean notificationsActive) {
        this.notificationsActive = notificationsActive;
    }

}
