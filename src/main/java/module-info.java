module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires de.jensd.fx.glyphs.fontawesome;
    requires TrayNotification;
    requires static itextpdf;


    opens org.example.javafx to javafx.fxml;
    exports org.example.javafx;
    exports org.example.javafx.controllers;
    opens org.example.javafx.controllers to javafx.fxml;
    exports org.example.javafx.dao;
    opens org.example.javafx.dao to javafx.fxml;
    exports org.example.javafx.entities;
    opens org.example.javafx.entities to javafx.fxml;
}