/**
 * Module principal de l'application Digital Banking
 */
module com.banking.digitalbankingproject {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    // Base de données
    requires java.sql;
    requires mysql.connector.java;
    
    // Utilitaires
    requires static lombok;
    requires jbcrypt;
    requires org.slf4j;

    // Exports
    exports com.banking.digitalbankingproject;
    exports com.banking.digitalbankingproject.controller;
    exports com.banking.digitalbankingproject.entity;
    exports com.banking.digitalbankingproject.service;
    exports com.banking.digitalbankingproject.enums;
    exports com.banking.digitalbankingproject.tools;
    exports com.banking.digitalbankingproject.util;
    exports com.banking.digitalbankingproject.database;
    
    // Opens pour JavaFX
    opens com.banking.digitalbankingproject to javafx.fxml;
    opens com.banking.digitalbankingproject.controller to javafx.fxml;
    opens com.banking.digitalbankingproject.tools to javafx.fxml;
    opens com.banking.digitalbankingproject.enums to javafx.fxml;
    opens com.banking.digitalbankingproject.util to javafx.fxml;
}