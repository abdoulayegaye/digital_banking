module com.banking.digitalbankingproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires TrayNotification;
    requires jbcrypt;


    opens com.banking.digitalbankingproject to javafx.fxml;
    exports com.banking.digitalbankingproject;
}