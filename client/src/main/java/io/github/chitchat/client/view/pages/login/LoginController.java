package io.github.chitchat.client.view.pages.login;

import com.google.inject.Inject;
import io.github.chitchat.client.config.Settings;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

@Log4j2
@NoArgsConstructor
public class LoginController implements Initializable {
    @Inject private Settings settings;

    @FXML private CustomTextField textFieldUser;
    @FXML private CustomPasswordField textFieldPassword;
    @FXML private Button buttonLogin;

    private String ephemeralPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(
                () -> {
                    textFieldUser.requestFocus();
                    textFieldUser.setText(settings.getUsername());
                });

        // Require non-empty username and password
        buttonLogin
                .disableProperty()
                .bind(
                        textFieldUser
                                .textProperty()
                                .isEmpty()
                                .or(textFieldPassword.textProperty().isEmpty()));

        // Show password on mouse press and hide on mouse release
        textFieldPassword
                .getRight()
                .setOnMousePressed(
                        _ -> {
                            ephemeralPassword = textFieldPassword.getText();
                            textFieldPassword.setPromptText(ephemeralPassword);
                            textFieldPassword.clear();
                        });
        textFieldPassword
                .getRight()
                .setOnMouseReleased(
                        _ -> {
                            textFieldPassword.setText(ephemeralPassword);
                            textFieldPassword.setPromptText("Password");
                        });

        // Login on Enter key press
        textFieldPassword.setOnAction(
                _ -> {
                    if (!buttonLogin.isDisabled()) login();
                });
    }

    public void login() {
        log.info("Logging in as {}", textFieldUser.getText());
        settings.setUsername(textFieldUser.getText());
    }
}
