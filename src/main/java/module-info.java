module com.example.projet_java_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires TrayNotification;
    requires static lombok;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires jdk.jfr;
    requires pdfbox;


    opens com.example.projet_java_fx to javafx.fxml;
    exports com.example.projet_java_fx;
    exports com.example.projet_java_fx.entity;
    opens com.example.projet_java_fx.entity to javafx.fxml;
    exports com.example.projet_java_fx.database;
    opens com.example.projet_java_fx.database to javafx.fxml;
    exports com.example.projet_java_fx.controllers;
    opens com.example.projet_java_fx.controllers to javafx.fxml;
    exports com.example.projet_java_fx.tools;
    opens com.example.projet_java_fx.tools to javafx.fxml;
}