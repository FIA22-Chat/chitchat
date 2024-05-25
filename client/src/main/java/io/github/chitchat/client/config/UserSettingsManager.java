package io.github.chitchat.client.config;

import io.github.chitchat.common.storage.local.LocalStore;
import io.github.chitchat.common.storage.local.config.Evaluation;
import lombok.experimental.Delegate;

public class UserSettingsManager {
    private final transient LocalStore<UserSettings> store;
    @Delegate private final UserSettings instance;

    public UserSettingsManager() {
        store = new LocalStore<>(Evaluation.EAGER, UserSettings.class, UserSettings::new);
        this.instance = store.get();
    }

    public synchronized void save() {
        store.set(instance);
    }
}
