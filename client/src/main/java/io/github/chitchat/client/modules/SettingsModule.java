package io.github.chitchat.client.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.chitchat.client.config.SettingsContext;
import io.github.chitchat.common.CommonUtil;

public class SettingsModule extends AbstractModule {
    @Provides
    @Singleton
    public SettingsContext provideSettings(@Named("AppName") String appName) {
        var path = CommonUtil.getUserAppDir(appName);
        return new SettingsContext(path);
    }
}
