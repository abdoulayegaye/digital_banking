package com.banking.digitalbankingproject.tools;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;

/**
 * Classe utilitaire pour gérer les notifications de l'application
 */
public class Notification {

    public static void show(String title, String message, NotificationType type) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        
        Label label = new Label(message);
        label.setStyle(
            "-fx-background-color: " + getBackgroundColor(type) + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 15px;" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 5px;"
        );
        
        StackPane root = new StackPane(label);
        root.setStyle("-fx-background-color: transparent;");
        root.setAlignment(Pos.BOTTOM_RIGHT);
        
        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);
        
        // Position en bas à droite de l'écran
        stage.setX(javafx.stage.Screen.getPrimary().getVisualBounds().getMaxX() - 350);
        stage.setY(javafx.stage.Screen.getPrimary().getVisualBounds().getMaxY() - 100);
        
        stage.show();
        
        // Disparaît après 3 secondes
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> stage.close());
        delay.play();
    }
    
    public static void NotifSuccess(String title, String message) {
        show(title, message, NotificationType.SUCCESS);
    }
    
    public static void NotifError(String title, String message) {
        show(title, message, NotificationType.ERROR);
    }
    
    public static void NotifInfo(String title, String message) {
        show(title, message, NotificationType.INFO);
    }
    
    public static void NotifWarning(String title, String message) {
        show(title, message, NotificationType.WARNING);
    }
    
    private static String getBackgroundColor(NotificationType type) {
        switch (type) {
            case SUCCESS: return "#2ecc71";
            case ERROR: return "#e74c3c";
            case WARNING: return "#f1c40f";
            case INFO: return "#3498db";
            default: return "#95a5a6";
        }
    }
    
    public enum NotificationType {
        SUCCESS,
        ERROR,
        WARNING,
        INFO
    }
}
