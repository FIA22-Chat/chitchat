package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.MessageDAO;
import io.github.chitchat.common.storage.database.dao.MessageDAOImpl;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannel;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannelListener;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class MessageService extends CacheableIndexableService<MessageDAO, Message>
        implements ServiceChannelListener<Message> {

    public MessageService(Jdbi db, int cacheSize) {
        super(new MessageDAOImpl.OnDemand(db), cacheSize);
    }

    public MessageService(
            Jdbi db, int cacheSize, @NotNull ServiceChannel<Message> messageServiceChannel) {
        this(db, cacheSize);
        messageServiceChannel.register(this);
    }

    @Override
    public void ReceiveUpdate(Message model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void ReceiveRemove(@NotNull Message model) {
        cache.invalidate(model.getId());
    }
}
