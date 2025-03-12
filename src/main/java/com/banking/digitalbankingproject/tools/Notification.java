package com.banking.digitalbankingproject.tools;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {
    public enum NotificationType {
        SUCCESS, ERROR, WARNING, INFO
    }

    public static void showNotification(String title, String text, NotificationType type) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(5));

        switch (type) {
            case SUCCESS:
                notification.showInformation();
                break;
            case ERROR:
                notification.showError();
                break;
            case WARNING:
                notification.showWarning();
                break;
            case INFO:
                notification.showInformation();
                break;
        }
    }
}
