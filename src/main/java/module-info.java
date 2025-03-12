module com.banking.digitalbankingproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires static lombok;
    requires jbcrypt;
    requires java.naming;

    opens com.banking.digitalbankingproject to javafx.fxml;
    exports com.banking.digitalbankingproject;

    exports com.banking.digitalbankingproject.controller;
    opens com.banking.digitalbankingproject.controller to javafx.fxml;

    exports com.banking.digitalbankingproject.entity;
    opens com.banking.digitalbankingproject.entity to javafx.fxml;

    exports com.banking.digitalbankingproject.tools;
    opens com.banking.digitalbankingproject.tools to javafx.fxml;

    exports com.banking.digitalbankingproject.utils;
    opens com.banking.digitalbankingproject.utils to javafx.fxml;
}