package io.github.chitchat.client;

import io.github.chitchat.client.config.UserSettingsManager;
import io.github.chitchat.client.view.SceneController;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class App extends Application {
    private static final String APP_NAME = "ChitChat";
    private static UserSettingsManager settings;

    public static void main(String[] args) {
        log.info("Starting client preloader...");

        // Sets the global default path for all local stores
        System.setProperty(
                "STORE_HOME",
                Path.of(System.getenv("LOCALAPPDATA"), APP_NAME)
                        .toString()); // todo use util impl instead
        settings = new UserSettingsManager();

        launch();
    }

    @Override
    public void start(@NotNull Stage primaryStage) {
        log.info("Starting client GUI...");

        // Save user settings on quit
        primaryStage.setOnCloseRequest(
                _ -> {
                    settings.setStageX(primaryStage.getX());
                    settings.setStageY(primaryStage.getY());
                    settings.setSceneWidth(primaryStage.getWidth());
                    settings.setSceneHeight(primaryStage.getHeight());
                    settings.setMaximized(primaryStage.isMaximized());
                    settings.setAlwaysOnTop(primaryStage.isAlwaysOnTop());
                    settings.setFullscreen(primaryStage.isFullScreen());
                    settings.save();
                    log.debug("Saved user settings");
                });

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

        // Load user settings
        primaryStage.setX(settings.getStageX());
        primaryStage.setY(settings.getStageY());
        primaryStage.setWidth(settings.getSceneWidth());
        primaryStage.setHeight(settings.getSceneHeight());
        primaryStage.setMaximized(settings.isMaximized());
        primaryStage.setAlwaysOnTop(settings.isAlwaysOnTop());
        primaryStage.setFullScreen(settings.isFullscreen());

        primaryStage.getIcons().addAll(getIcons());
        primaryStage.setTitle(APP_NAME);
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
