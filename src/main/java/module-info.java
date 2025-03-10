/**
 * Module principal de l'application Digital Banking
 */
module com.banking.digitalbankingproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.sql;

    requires static lombok;
    
    exports com.banking.digitalbankingproject;
    exports com.banking.digitalbankingproject.controller;
    exports com.banking.digitalbankingproject.entity;
    exports com.banking.digitalbankingproject.service;
    exports com.banking.digitalbankingproject.tools;
    exports com.banking.digitalbankingproject.enums;

    opens com.banking.digitalbankingproject to javafx.fxml;
    opens com.banking.digitalbankingproject.controller to javafx.fxml;
    opens com.banking.digitalbankingproject.entity to javafx.fxml;
    opens com.banking.digitalbankingproject.service to javafx.fxml;
    opens com.banking.digitalbankingproject.tools to javafx.fxml;
    opens com.banking.digitalbankingproject.enums to javafx.fxml;
}