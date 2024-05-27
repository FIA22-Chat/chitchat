package io.github.chitchat.common.storage.database.service.channel;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;

/**
 * A listener for a service channel.
 *
 * @param <T> The channel type
 */
public interface ServiceChannelListener<T extends IndexableModel> {
    void receiveUpdate(T model);

    void receiveRemove(T model);
}
