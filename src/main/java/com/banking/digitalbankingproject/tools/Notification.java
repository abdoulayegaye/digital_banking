package com.banking.digitalbankingproject.tools;

import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class Notification {

    /**
     * Affiche une notification de succès.
     *
     * @param titre   Le titre de la notification.
     * @param message Le message de la notification.
     */
    public static void NotifSuccess(String titre, String message) {
        NotificationType type = NotificationType.SUCCESS;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(titre);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndDismiss(Duration.seconds(2));
    }

    /**
     * Affiche une notification d'erreur.
     *
     * @param titre   Le titre de la notification.
     * @param message Le message de la notification.
     */
    public static void NotifError(String titre, String message) {
        NotificationType type = NotificationType.ERROR;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(titre);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndDismiss(Duration.seconds(2));
    }
}