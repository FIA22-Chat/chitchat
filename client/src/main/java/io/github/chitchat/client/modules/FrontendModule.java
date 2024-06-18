package io.github.chitchat.client.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.chitchat.client.config.SettingsContext;
import io.github.chitchat.client.view.routing.Router;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class FrontendModule extends AbstractModule {
    private final Stage primaryStage;

    public FrontendModule(@NotNull Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Provides
    @Singleton
    public FXMLLoader provideFXMLLoader(@NotNull Injector injector) {
        var loader = new FXMLLoader();
        loader.setControllerFactory(injector::getInstance);

        return loader;
    }

    @Provides
    @Singleton
    public @Named("PrimaryStage") Stage providePrimaryStage() {
        return primaryStage;
    }

    @Provides
    @Singleton
    public Router provideRouter(FXMLLoader loader, @Named("PrimaryStage") Stage stage, SettingsContext settingsContext) {
        return new Router(loader, stage, settingsContext);
    }
}
