package io.github.chitchat.client;

import com.google.inject.Guice;
import io.github.chitchat.client.config.Settings;
import io.github.chitchat.client.config.UserContext;
import io.github.chitchat.client.modules.AppModule;
import io.github.chitchat.client.modules.FrontendModule;
import io.github.chitchat.client.modules.SettingsModule;
import io.github.chitchat.client.modules.UserModule;
import io.github.chitchat.client.view.routing.Page;
import io.github.chitchat.client.view.routing.Router;
import java.util.Objects;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class App extends Application {
    private static final String APP_NAME = "ChitChat";
    private static final double STAGE_MIN_WIDTH = 300;
    private static final double STAGE_MIN_HEIGHT = 300;

    private Settings settings;
    private UserContext userContext;
    private Stage stage;

    public static void main(String[] args) {
        log.info("Starting client preloader...");

        launch();
    }

    @Override
    public void start(@NotNull Stage stage) {
        log.info("Starting client GUI...");
        var injector =
                Guice.createInjector(
                        new AppModule(APP_NAME),
                        new FrontendModule(stage),
                        new SettingsModule(),
                        new UserModule());
        this.stage = stage;
        this.settings = injector.getInstance(Settings.class);
        this.userContext = injector.getInstance(UserContext.class);

        var router = injector.getInstance(Router.class);
        router.navigateTo(Page.LOGIN);

        settings.applyStageSettings(stage);
        stage.setMinWidth(STAGE_MIN_WIDTH);
        stage.setMinHeight(STAGE_MIN_HEIGHT);
        stage.getIcons().addAll(getIcons());
        stage.setTitle(APP_NAME);
        stage.show();
    }

    @Override
    public void stop() {
        log.info("Stopping client...");

        settings.storeStageSettings(stage);
        settings.save();
        userContext.save();
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
                                            "assets/logo/logo-" + sizes[i] + "x.png")));

        return list;
    }
}
