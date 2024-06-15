package io.github.chitchat.client.view.pages.settings;

import io.github.chitchat.client.config.UserContext;
import jakarta.inject.Inject;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class SettingsController {
    @Inject private UserContext userContext;

    @FXML private Label headlineSettings;
    @FXML private TextField usernameField;
    @FXML private TextField statusField;
    @FXML private TextField imagePathField;
    @FXML private Button changeUsername;

    @FXML private VBox profilePane;
    @FXML private VBox accountPane;
    @FXML private VBox privacyPane;
    @FXML private VBox notificationsPane;

    @FXML
    private void initialize() {
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
            imagePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void showProfile() {
        profilePane.setVisible(true);
    }
}
