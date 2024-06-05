package io.github.chitchat.common.storage.database.service.channel;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;

/**
 * A broadcaster for a service channel.
 *
 * @param <T> The channel type
 */
public interface ServiceChannelBroadcaster<T extends IndexableModel> {
    void broadcastUpdate(T model);

    void broadcastRemove(T model);
}
