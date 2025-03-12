package com.banking.digitalbankingproject.tools;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    public static void handleException(Exception e, String title, String message) {
        logger.error(message, e);
        Platform.runLater(() -> {
            Notification.NotifError(title, message);
        });
    }

    public static void handleDatabaseException(Exception e) {
        handleException(e, "Erreur Base de données", 
            "Une erreur est survenue lors de l'accès à la base de données");
    }

    public static void handleApplicationException(Exception e) {
        handleException(e, "Erreur Application", 
            "Une erreur inattendue est survenue");
    }
} 