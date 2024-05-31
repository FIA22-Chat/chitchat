package io.github.chitchat.client.config;

import io.github.chitchat.common.storage.local.LocalStore;
import io.github.chitchat.common.storage.local.config.Evaluation;
import lombok.experimental.Delegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserSettingsManager {
    private static final Logger log = LogManager.getLogger(UserSettingsManager.class);

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
