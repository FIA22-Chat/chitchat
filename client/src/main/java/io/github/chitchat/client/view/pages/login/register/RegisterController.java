package io.github.chitchat.client.view.pages.login.register;

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
public class RegisterController implements Initializable {
    @Inject private Settings settings;
    @Inject private Router router;

    @FXML private ChitTextField textFieldUser;
    @FXML private ChitPasswordField textFieldPassword;
    @FXML private ChitPasswordField textFieldPasswordConfirm;
    @FXML private Button buttonRegister;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> textFieldUser.requestFocus());

        // Require non-empty username and password
        buttonRegister
                .disableProperty()
                .bind(
                        textFieldUser
                                .textProperty()
                                .isEmpty()
                                .or(textFieldPassword.textProperty().isEmpty())
                                .or(textFieldPasswordConfirm.textProperty().isEmpty()));

        // Register on Enter key press
        textFieldPasswordConfirm.setOnAction(
                _ -> {
                    if (!buttonRegister.isDisabled()) register();
                });
    }

    @FXML
    private void register() {
        log.info("Registering user: {}", textFieldUser.getText());

        if (!textFieldPassword.getText().equals(textFieldPasswordConfirm.getText())) {
            textFieldPassword.setError(true);
            textFieldPasswordConfirm.setError(true);
            return;
        } else {
            textFieldPassword.setError(false);
            textFieldPasswordConfirm.setError(false);
        }

        // todo register user
        settings.setUsername(textFieldUser.getText());
        router.navigateTo(Page.MAIN);
    }

    @FXML
    private void back() {
        router.navigateTo(Page.LOGIN);
    }
}
