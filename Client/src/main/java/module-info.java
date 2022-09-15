module ChatApp.ClientServer {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;

    opens ChatApp.ClientServer to javafx.fxml;
    exports ChatApp.ClientServer;
}
