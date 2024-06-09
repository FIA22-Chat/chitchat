package io.github.chitchat.client.view.pages.main;

import io.github.chitchat.client.view.components.ChitGroup;
import io.github.chitchat.client.view.components.ChitGroupCell;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class MainController implements Initializable {
    @FXML public Button callButton;
    @FXML private ListView<ChitGroup> groupList;

    @FXML private TextArea chatinput;

    @FXML private TextArea inputArea;

    @FXML private Button sendButton;

    @FXML private Label lastOnlineStatus;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupList.setCellFactory(_ -> new ChitGroupCell());
        groupList
                .getItems()
                .addAll(
                        new ChitGroup(
                                "aaaa",
                                "aaaa",
                                "/io/github/chitchat/client/assets/logo/logo-256x.png"),
                        new ChitGroup(
                                "aaaa",
                                "aaaa",
                                "/io/github/chitchat/client/assets/logo/logo-24x.png"),
                        new ChitGroup(
                                "aaaa",
                                "aaaa",
                                "/io/github/chitchat/client/assets/logo/logo-60x.png"));

        sendButton.setOnAction(_ -> sendMessage());
    }

    @FXML
    private void sendMessage() {}

    @FXML
    private void loadChatMessages() {}
}
