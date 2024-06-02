package io.github.chitchat.client.config;

import io.github.chitchat.common.storage.local.LocalStore;
import io.github.chitchat.common.storage.local.config.Evaluation;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserSettingsManager {
    private final transient LocalStore<UserSettings> store;
    @Delegate private final UserSettings instance;

    public UserSettingsManager() {
        log.debug("Loading user settings...");
        this.store = new LocalStore<>(Evaluation.EAGER, UserSettings.class, UserSettings::new);
        this.instance = store.get();

        log.trace("Loaded {}", instance);
    }

    public synchronized void save() {
        log.trace("Saving {}", instance);
        store.set(instance);
    }
}
