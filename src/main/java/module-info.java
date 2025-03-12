module com.banking.com.banking.digitalbankingproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires TrayNotification;
    requires jbcrypt;
    requires layout;
    requires kernel;
    requires io;
    requires org.apache.pdfbox;
    requires barcodes;


    opens com.banking.digitalbankingproject to javafx.fxml;
    exports com.banking.digitalbankingproject;

    exports com.banking.digitalbankingproject.controller;
    opens com.banking.digitalbankingproject.controller to javafx.fxml;

    exports com.banking.digitalbankingproject.entity;
    opens com.banking.digitalbankingproject.entity to javafx.fxml;
}