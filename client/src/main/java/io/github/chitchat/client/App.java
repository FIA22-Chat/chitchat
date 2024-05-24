package io.github.chitchat.client;

import io.github.chitchat.client.view.SceneController;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class App extends Application {
    private static final Logger log = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        log.info("Starting client...");

        var basePath = "pages/";
        var pages =
                List.of(
                        basePath + "login/login.fxml",
                        basePath + "main/main.fxml",
                        basePath + "settings/settings.fxml");
        SceneController sceneController = new SceneController(primaryStage, pages);
        try {
            sceneController.navigateTo(0);
        } catch (IOException e) {
            // We can't recover from this, so we log the error and throw a runtime exception
            log.fatal("Failed to load login page", e);
            throw new RuntimeException(e);
        }

        primaryStage.getIcons().addAll(getIcons());
        primaryStage.setTitle("Chat");
        primaryStage.show();
    }

    private Image @NotNull [] getIcons() {
        var list = new Image[14];
        var sizes = new int[] {16, 20, 24, 30, 32, 36, 40, 48, 60, 64, 72, 80, 96, 256};

        log.trace("Loading {} icons", sizes.length);
        for (var i = 0; i < sizes.length; i++)
            list[i] =
                    new Image(
                            Objects.requireNonNull(
                                    App.class.getResourceAsStream(
                                            "icons/logo/logo-" + sizes[i] + "x.png")));

        return list;
    }
}
