package io.github.chitchat.client.view.pages.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class LoginController implements Initializable {
    @FXML private TextField textFieldUser;
    @FXML private PasswordField textFieldPassword;
    @FXML private Button loginButton;

    private String password;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> textFieldUser.requestFocus());

        loginButton.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                e -> {
                    password = textFieldPassword.getText();
                    textFieldPassword.clear();
                    textFieldPassword.setPromptText(password);
                });
        loginButton.addEventFilter(
                MouseEvent.MOUSE_RELEASED,
                e -> {
                    textFieldPassword.setText(password);
                    textFieldPassword.setPromptText("Password");
                });
    }

    public void login() {}
}
