module io.github.chitchat.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.apache.logging.log4j;

    opens io.github.chitchat.client to
            javafx.fxml;

    exports io.github.chitchat.client;
}
