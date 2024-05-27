package io.github.chitchat.common.storage.database.service.channel;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.util.ArrayList;

/**
 * Used to broadcast updates and removals of models to listeners. Useful for keeping caches in sync.
 *
 * @param <T> The channel type
 */
public class ServiceChannel<T extends IndexableModel> implements ServiceChannelBroadcaster<T> {
    private final ArrayList<ServiceChannelListener<T>> listeners = new ArrayList<>();

    public void register(ServiceChannelListener<T> listener) {
        listeners.add(listener);
    }

    public void unregister(ServiceChannelListener<T> listener) {
        listeners.remove(listener);
    }

    @Override
    public void broadcastUpdate(T model) {
        listeners.forEach(listener -> listener.receiveUpdate(model));
    }

    @Override
    public void broadcastRemove(T model) {
        listeners.forEach(listener -> listener.receiveRemove(model));
    }
}
