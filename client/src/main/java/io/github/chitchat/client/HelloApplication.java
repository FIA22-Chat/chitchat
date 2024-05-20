package io.github.chitchat.client;

import java.io.IOException;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class HelloApplication extends Application {
    private static final Logger log = LogManager.getLogger(HelloApplication.class);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        log.info("Starting client...");

        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("pages/main/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        primaryStage.getIcons().addAll(getIcons());
        primaryStage.setTitle("Chat");
        primaryStage.setScene(scene);
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
                                    HelloApplication.class.getResourceAsStream(
                                            "icons/logo/logo-" + sizes[i] + "x.png")));

        return list;
    }
}
