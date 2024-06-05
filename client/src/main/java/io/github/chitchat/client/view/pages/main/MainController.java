package io.github.chitchat.client.view.pages.main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class MainController {
    @FXML public Button callButton;
    @FXML private ListView<String> chatList;

    @FXML private TextArea chatinput;

    @FXML private TextArea inputArea;

    @FXML private Button sendButton;

    @FXML private Label lastOnlineStatus;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        sendButton.setOnAction(event -> sendMessage());
    }

    @FXML
    private void sendMessage() {}

    @FXML
    private void loadChatMessages() {}
}
