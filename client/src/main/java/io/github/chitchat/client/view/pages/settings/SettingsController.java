package io.github.chitchat.client.view.pages.settings;

import io.github.chitchat.client.config.UserContext;
import io.github.chitchat.client.view.routing.Page;
import io.github.chitchat.client.view.routing.Router;
import jakarta.inject.Inject;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class SettingsController {
    @Inject private UserContext userContext;
    @Inject private Router router;
    @FXML private Label headlineSettings;
    @FXML private TextField usernameField;
    @FXML private TextField statusField;
    @FXML private TextField imagePathField;
    @FXML private Button changeUsername;

    @FXML private ImageView profileImageView;
    @FXML private Circle profileCircle;

    @FXML private VBox profilePane;
    @FXML private VBox accountPane;
    @FXML private VBox privacyPane;
    @FXML private VBox notificationsPane;

    @FXML
    private void initialize() {
        profileCircle = new Circle(50, 50, 50);
        profileImageView.setClip(profileCircle);

        profileImageView.setImage(new Image("io/github/chitchat/client/assets/logo/logo-256x.png"));
        usernameField.setText(userContext.getUsername());
    }

    @FXML
    private void saveSettings() {}

    @FXML
    private void cancelSettings() {}

    @FXML
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toURL().toExternalForm());

                profileImageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void showProfile() {
        profilePane.setVisible(true);
    }

    public void backToMain() {
        router.navigateTo(Page.MAIN);
    }
}
