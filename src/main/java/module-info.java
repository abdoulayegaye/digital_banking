module com.banking.digitalbankingproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires jbcrypt;
    requires itextpdf;
    requires mysql.connector.j;

    opens com.banking.digitalbankingproject to javafx.fxml;
    opens com.banking.digitalbankingproject.controller to javafx.fxml;
    opens com.banking.digitalbankingproject.entity to javafx.base;

    exports com.banking.digitalbankingproject;
    exports com.banking.digitalbankingproject.controller;
    exports com.banking.digitalbankingproject.entity;
    exports com.banking.digitalbankingproject.service;
    exports com.banking.digitalbankingproject.enums;
}