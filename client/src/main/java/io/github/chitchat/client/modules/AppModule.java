package io.github.chitchat.client.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.jetbrains.annotations.NotNull;

public class AppModule extends AbstractModule {
    private final String APP_NAME;

    public AppModule(@NotNull String appName) {
        this.APP_NAME = appName;
    }

    @Provides
    @Singleton
    public @Named("AppName") String provideAppName() {
        return APP_NAME;
    }
}
