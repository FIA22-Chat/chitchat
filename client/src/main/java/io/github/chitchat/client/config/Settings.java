package io.github.chitchat.client.config;

import io.github.chitchat.common.storage.local.LocalStore;
import io.github.chitchat.common.storage.local.config.Evaluation;
import java.nio.file.Path;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Settings {
    private final transient LocalStore<SettingsData> store;
    @Delegate private final SettingsData instance;

    public Settings(Path basePath) {
        log.debug("Loading user settings...");
        this.store =
                new LocalStore<>(
                        SettingsData.class.getSimpleName(),
                        Evaluation.EAGER,
                        basePath,
                        SettingsData.class,
                        SettingsData::new);
        this.instance = store.get();

        log.trace("Loaded {}", instance);
    }

    public synchronized void save() {
        log.trace("Saving {}", instance);
        store.set(instance);
    }
}
