package io.github.chitchat.client.view;

import io.github.chitchat.client.App;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SceneController {
    private static final double SCENE_WIDTH = 1400;
    private static final double SCENE_HEIGHT = 800;

    private final List<String> pages;

    private final Stage primaryStage;
    private final FXMLLoader fxmlLoader;
    private int currentIndex;

    /**
     * Constructs a new SceneController with the specified primary stage, pages, and dimensions.
     *
     * @param primaryStage the primary stage
     * @param pages the list of pages that can be navigated to
     */
    public SceneController(Stage primaryStage, List<String> pages) {
        this.primaryStage = primaryStage;
        this.pages = pages;
        this.fxmlLoader = new FXMLLoader();
    }

    /**
     * Navigates to the previous page in the list.
     *
     * @throws IndexOutOfBoundsException if the index is outside the bounds of the page list
     * @throws IOException if the page cannot be loaded
     */
    public synchronized void navigateBack() throws IndexOutOfBoundsException, IOException {
        navigateTo(--currentIndex);
    }

    /**
     * Navigates to the next page in the list.
     *
     * @throws IndexOutOfBoundsException if the index is outside the bounds of the page list
     * @throws IOException if the page cannot be loaded
     */
    public synchronized void navigateForward() throws IndexOutOfBoundsException, IOException {
        navigateTo(++currentIndex);
    }

    /**
     * Navigates to the page at the specified index.
     *
     * @param index the index of the page to navigate to
     * @throws IndexOutOfBoundsException if the index is outside the bounds of the page list
     * @throws IOException if the page cannot be loaded
     * @apiNote uses synchronized to prevent multiple navigations from occurring at the same time
     */
    public synchronized void navigateTo(int index) throws IndexOutOfBoundsException, IOException {
        if (index < 0)
            throw new IndexOutOfBoundsException(
                    "Unable to navigate backwards, would be outside of bounds: 0");
        if (index > pages.size())
            throw new IndexOutOfBoundsException(
                    "Unable to navigate forwards, would be outside of bounds: " + pages.size());
        String path = pages.get(index);
        log.debug("Loading Page: {}", path);

        fxmlLoader.setLocation(App.class.getResource(path));
        primaryStage.setScene(new Scene(fxmlLoader.load(), SCENE_WIDTH, SCENE_HEIGHT));
        currentIndex = index;
    }

    /**
     * Shows a popup with the specified path, width, and height. This will block any events from
     * reaching the primary stage.
     *
     * @param path the path to the FXML file
     * @param width the width of the popup
     * @param height the height of the popup
     * @throws IOException if the popup cannot be loaded
     */
    public synchronized void showPopup(String path, double width, double height)
            throws IOException {
        fxmlLoader.setLocation(App.class.getResource(path));
        var scenePopup = new Scene(fxmlLoader.load(), width, height);

        var stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scenePopup);
        stage.show();
    }
}
