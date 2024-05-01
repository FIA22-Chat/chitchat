module io.github.chitchat.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens io.github.chitchat.client to javafx.fxml;
    exports io.github.chitchat.client;
}