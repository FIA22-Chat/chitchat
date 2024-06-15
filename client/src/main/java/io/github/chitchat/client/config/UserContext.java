package io.github.chitchat.client.config;

import io.github.chitchat.client.config.common.IDelegatedStore;
import io.github.chitchat.common.storage.local.LocalStore;
import io.github.chitchat.common.storage.local.config.Evaluation;
import java.nio.file.Path;
import lombok.experimental.Delegate;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserContext implements IDelegatedStore {
    private final transient LocalStore<UserContextData> store;
    @Delegate private final UserContextData instance;

    public UserContext(Path basePath) {
        log.debug("Loading user ...");
        this.store =
                new LocalStore<>(
                        UserContextData.class.getSimpleName(),
                        Evaluation.EAGER,
                        basePath,
                        UserContextData.class,
                        UserContextData::new);
        this.instance = store.get();

        log.trace("Loaded {}", instance);
    }

    public synchronized void save() {
        log.trace("Saving {}", instance);
        store.set(instance);
    }
}
