package com.banking.digitalbankingproject.utils;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationUtils {
    
    public static void showSuccess(String title, String message) {
        show(title, message, "SUCCESS");
    }
    
    public static void showError(String title, String message) {
        show(title, message, "ERROR");
    }
    
    public static void showInfo(String title, String message) {
        show(title, message, "INFORMATION");
    }
    
    private static void show(String title, String message, String type) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
                
        notification.show();
    }
} 