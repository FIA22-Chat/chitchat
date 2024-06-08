package io.github.chitchat.client.view.pages.login;

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
    @FXML private CustomTextField textFieldUser;
    @FXML private CustomPasswordField textFieldPassword;
    @FXML private Button buttonLogin;

    private String ephemeralPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> textFieldUser.requestFocus());

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
    }

    public void login() {}
}
