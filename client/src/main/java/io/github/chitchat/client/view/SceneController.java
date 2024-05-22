package io.github.chitchat.client.view;

import io.github.chitchat.client.App;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SceneController {

    private static final Logger log = LogManager.getLogger(SceneController.class);
    private FXMLLoader loader;
    private final Stage primaryStage;
    private Stage primaryModalStage;
    private final List<String> pagesPaths;
    private final double defaultSceneWidth;
    private final double defaultSceneHeight;
    private int currentIndex = 0;

    public SceneController(
            Stage primaryStage,
            List<String> pagesPaths,
            double defaultSceneWidth,
            double defaultSceneHeight) {
        this.primaryStage = primaryStage;
        this.pagesPaths = pagesPaths;
        this.defaultSceneWidth = defaultSceneWidth;
        this.defaultSceneHeight = defaultSceneHeight;
    }

    public void navigateBack() throws IndexOutOfBoundsException, IOException {
        if (currentIndex - 1 < 0)
            throw new IndexOutOfBoundsException(
                    "Unable to navigate backwards, would be outside of bounds");

        navigateTo(--currentIndex);
    }

    public void navigateForward() throws IndexOutOfBoundsException, IOException {
        if (currentIndex + 1 > pagesPaths.size())
            throw new IndexOutOfBoundsException(
                    "Unable to navigate forwards, would be outside of bounds");

        navigateTo(++currentIndex);
    }

    public void navigateTo(int index) throws IndexOutOfBoundsException, IOException {
        if (index < 0)
            throw new IndexOutOfBoundsException(
                    "Unable to navigate backwards, would be outside of bounds");
        if (index > pagesPaths.size())
            throw new IndexOutOfBoundsException(
                    "Unable to navigate forwards, would be outside of bounds");
        String path = pagesPaths.get(index);
        log.debug("Loading Page: {}", path);
        var loaded = new FXMLLoader(App.class.getResource(path));
        primaryStage.setScene(new Scene(loaded.load(), defaultSceneWidth, defaultSceneHeight));
        currentIndex = index;
    }

    public void showModal(String path, double width, double height) throws IOException {
        var loaded = new FXMLLoader(App.class.getResource(path));
        var scenePopup = new Scene(loaded.load(), width, height);

        primaryModalStage = new Stage();
        primaryModalStage.initModality(Modality.APPLICATION_MODAL);
        primaryModalStage.setScene(scenePopup);
        primaryModalStage.show();
    }

    public Stage getPrimaryModalStage() {
        return this.primaryModalStage;
    }
}
