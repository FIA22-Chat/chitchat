package io.github.chitchat.client.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.chitchat.client.view.routing.Router;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class FrontendModule extends AbstractModule {
    private final Stage primaryStage;
    private final List<String> pages;

    public FrontendModule(@NotNull Stage primaryStage, @NotNull List<String> pages) {
        this.primaryStage = primaryStage;
        this.pages = pages;
    }

    @Provides
    @Singleton
    public FXMLLoader provideFXMLLoader(Injector injector) {
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
    public @Named("Pages") List<String> providePages() {
        return pages;
    }

    @Provides
    @Singleton
    public Router provideRouter(
            FXMLLoader loader,
            @Named("PrimaryStage") Stage stage,
            @Named("Pages") List<String> pages) {
        return new Router(loader, stage, pages);
    }
}
