package io.github.chitchat.client.view.pages.login;

import com.google.inject.Inject;
import io.github.chitchat.client.config.Settings;
import io.github.chitchat.client.view.components.ChitPasswordField;
import io.github.chitchat.client.view.components.ChitTextField;
import io.github.chitchat.client.view.routing.Page;
import io.github.chitchat.client.view.routing.Router;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class LoginController implements Initializable {
    @Inject private Settings settings;
    @Inject private Router router;

    @FXML private ChitTextField textFieldUser;
    @FXML private ChitPasswordField textFieldPassword;
    @FXML private Button buttonLogin;

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

        // Login on Enter key press
        textFieldPassword.setOnAction(
                _ -> {
                    if (!buttonLogin.isDisabled()) login();
                });
    }

    @FXML
    private void login() {
        log.info("Logging in as {}", textFieldUser.getText());

        // todo auth user
        // todo on failure
        if (false) {
            textFieldUser.setError(true);
            textFieldPassword.setError(true);
            return;
        }

        settings.setUsername(textFieldUser.getText());
        router.navigateTo(Page.MAIN);
    }

    @FXML
    private void register() {
        router.navigateTo(Page.REGISTER);
    }
}
