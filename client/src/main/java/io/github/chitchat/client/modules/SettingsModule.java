package io.github.chitchat.client.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.chitchat.client.config.Settings;
import java.nio.file.Path;

public class SettingsModule extends AbstractModule {
    @Provides
    @Singleton
    public Settings provideSettings(@Named("AppName") String appName) {
        var path = Path.of(System.getenv("LOCALAPPDATA"), appName);
        return new Settings(path);
    }
}
