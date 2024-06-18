package io.github.chitchat.client.view.pages.settings;

import io.github.chitchat.client.config.SettingsContext;
import io.github.chitchat.client.config.UserContext;
import io.github.chitchat.client.view.routing.Page;
import io.github.chitchat.client.view.routing.Router;
import jakarta.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@NoArgsConstructor
public class SettingsController {
    @Inject private UserContext userContext;
    @Inject private SettingsContext settingsContext;
    @Inject private Router router;

    @FXML private Label headlineSettings;
    @FXML private TextField usernameField;
    @FXML private TextField statusField;
    @FXML private TextField imagePathField;
    @FXML private Button changeUsername;
    @FXML private ComboBox<Locale> languageBox;

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

        setupLanguageComboBox(languageBox);
    }

    private void setupLanguageComboBox(@NotNull ComboBox<Locale> languageBox) {
        // Setup display functionality for the language box
        languageBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(Locale locale) {
                        return locale.getDisplayLanguage() + " - " + locale.getLanguage();
                    }

                    @Override
                    public Locale fromString(String s) {
                        return null;
                    }
                });
        languageBox.setCellFactory(
                _ ->
                        new ListCell<>() {
                            @Override
                            protected void updateItem(Locale item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setGraphic(null);
                                } else {
                                    setText(item.getDisplayLanguage(settingsContext.getLocale()));
                                }
                            }
                        });

        // Add all available languages to the language box
        languageBox
                .getItems()
                .addAll(
                        Arrays.stream(Locale.getISOLanguages())
                                .map(Locale::forLanguageTag)
                                .sorted(Comparator.comparing(Locale::getDisplayLanguage))
                                .toList());
        // Select the current language
        languageBox.getSelectionModel().select(settingsContext.getLocale());
    }

    @FXML
    private void saveSettings() {
        userContext.setUsername(usernameField.getText());
        settingsContext.save();

        // Re-render if a language change occurred
        if (!settingsContext.getLocale().equals(languageBox.getValue())) {
            settingsContext.setLocale(languageBox.getValue());
            router.clearCache();
            router.reRenderCurrentPage();
        }
    }

    @FXML
    private void cancelSettings() {}

    @FXML
    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        var selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toURL().toExternalForm());
                profileImageView.setImage(image);
            } catch (IOException e) {
                log.error("Failed to load profile image", e);
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
