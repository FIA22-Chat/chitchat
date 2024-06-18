package io.github.chitchat.client.view.pages.main;

import com.google.inject.Inject;
import io.github.chitchat.client.view.components.ChitGroup;
import io.github.chitchat.client.view.components.ChitGroupCell;
import io.github.chitchat.client.view.components.ChitMessageBox;
import io.github.chitchat.client.view.components.ChitMessageBoxCell;
import io.github.chitchat.client.view.routing.Page;
import io.github.chitchat.client.view.routing.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.WebRtcCallController;

@Log4j2
@NoArgsConstructor
public class MainController implements Initializable {
    @Inject private Router router;

    @FXML private ListView<ChitGroup> groupList;
    @FXML private ImageView selectedGroupImage;
    @FXML private Label selectedGroupName;
    @FXML private Label selectedGroupLastOnline;
    @FXML private Button callVideoButton;

    @FXML private ListView<ChitMessageBox> messageList;
    @FXML private TextField inputArea;
    @FXML private Button sendButton;
    @FXML private Button profileButton;
    @FXML private Button settingsButton;
    @FXML private Button logoutButton;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupList.setCellFactory(_ -> new ChitGroupCell());
        messageList.setCellFactory(_ -> new ChitMessageBoxCell());

        selectedGroupImage.setImage(
                new Image("/io/github/chitchat/client/assets/logo/logo-60x.png"));
        groupList
                .getItems()
                .addAll(
                        new ChitGroup(
                                "aaaa",
                                "aaaa",
                                "/io/github/chitchat/client/assets/logo/logo-256x.png"));

        inputArea.setOnAction(_ -> sendMessage());
        sendButton.setOnAction(_ -> sendMessage());
    }

    @FXML
    private void profile() {
        router.navigateTo(Page.PROFILE);
    }

    @FXML
    private void settings() {
        router.navigateTo(Page.SETTINGS);
    }

    @FXML
    private void logout() {
        router.navigateTo(Page.LOGIN);
    }

    @FXML
    private void sendMessage() {
        String message = inputArea.getText();
        if (message.isEmpty()) return;

        var box = new ChitMessageBox(message, Pos.CENTER_RIGHT);
        messageList.getItems().add(box);

        messageList.scrollTo(box);
        inputArea.clear();
    }

    @FXML
    private void onVideoCallButtonClick() {
        try {
            WebRtcCallController.call(1, "test");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void loadChatMessages() {}
}
