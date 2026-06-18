module com.metricai.gamevault {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens com.metricai.gamevault to javafx.fxml;
    opens com.metricai.gamevault.controller to javafx.fxml;
    opens com.metricai.gamevault.model to javafx.base;

    exports com.metricai.gamevault;
}