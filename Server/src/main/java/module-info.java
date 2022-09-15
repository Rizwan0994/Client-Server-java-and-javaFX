module ChatApp.Server {
    requires javafx.controls;
    requires javafx.fxml;

    opens ChatApp.Server to javafx.fxml;
    exports ChatApp.Server;
}
