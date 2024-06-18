package io.github.chitchat.client.view.routing;

import io.github.chitchat.client.App;
import java.util.HashMap;
import java.util.ResourceBundle;

import io.github.chitchat.client.config.SettingsContext;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class Router {
    private static final double SCENE_WIDTH = 1400;
    private static final double SCENE_HEIGHT = 800;

    private final HashMap<Page, Parent> pagesCache = new HashMap<>();
    private final FXMLLoader fxmlLoader;
    private final Stage primaryStage;
    @Getter private Page currentPage;

    private final SettingsContext settingsContext;

    /**
     * Constructs a new Router with the specified FXML loader, primary stage.
     *
     * @param fxmlLoader the FXML loader
     * @param primaryStage the primary stage
     */
    public Router(FXMLLoader fxmlLoader, Stage primaryStage, SettingsContext settingsContext) {
        this.fxmlLoader = fxmlLoader;
        this.primaryStage = primaryStage;
        this.settingsContext = settingsContext;
    }

    /**
     * Navigates to the page at the specified enum value.
     *
     * @param page the enum value of the page to navigate to
     * @throws RuntimeException if the page cannot be loaded
     * @apiNote uses synchronized to prevent multiple navigations from occurring at the same time
     */
    public synchronized void navigateTo(@NotNull Page page) {
        var rootScene = primaryStage.getScene();
        if (rootScene != null) {
            rootScene.setRoot(loadPage(page));
        } else {
            primaryStage.setScene(new Scene(loadPage(page), SCENE_WIDTH, SCENE_HEIGHT));
        }

        currentPage = page;
    }

    /**
     * Shows a popup with the page, width, and height. Due to {@link Modality#WINDOW_MODAL}, the
     * popup will block any events from reaching the parent window.
     *
     * @param page the enum value of the page to show
     * @param width the width of the popup
     * @param height the height of the popup
     * @throws RuntimeException if the page cannot be loaded
     */
    public void showPopup(@NotNull Page page, double width, double height) {
        var stage = new Stage();
        stage.initOwner(primaryStage);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(loadPage(page), width, height));
        stage.show();
    }

    private synchronized Parent loadPage(Page page) {
        return pagesCache.computeIfAbsent(
                page,
                k -> {
                    try {
                        log.trace("Cache miss on page: {}", k);
                        fxmlLoader.setLocation(App.class.getResource(k.getPath()));
                        log.warn(settingsContext.getLocale());
                        fxmlLoader.setResources(ResourceBundle.getBundle("io/github/chitchat/client/bundles/language", settingsContext.getLocale()));

                        fxmlLoader.setRoot(null);
                        fxmlLoader.setController(null);
                        return fxmlLoader.load();
                    } catch (Exception e) {
                        log.error("Failed to load page: {}", page, e);
                        throw new RuntimeException(e);
                    }
                });
    }
}
