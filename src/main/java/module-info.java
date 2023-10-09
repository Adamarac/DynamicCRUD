module Utility {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires commons.beanutils;
    requires java.base;

    opens Controllers to javafx.fxml;
    opens Utility to javafx.fxml;
    exports Utility;
    exports Controllers;
}
