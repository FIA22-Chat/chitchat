package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.MessageDAO;
import io.github.chitchat.common.storage.database.dao.MessageDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannel;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannelListener;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import java.util.List;
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

    public int count(User user) {
        return dao.count(user);
    }

    public int count(Group group) {
        return dao.count(group);
    }

    public int count(Group group, User user) {
        return dao.count(group, user);
    }

    public List<Message> getByUser(User user) {
        return dao.getByUser(user);
    }

    public List<Message> getByGroup(Group group) {
        return dao.getByGroup(group);
    }

    public List<Message> getByGroupUser(Group group, User user) {
        return dao.getByGroupUser(group, user);
    }

    @Override
    public void receiveUpdate(Message model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void receiveRemove(@NotNull Message model) {
        cache.invalidate(model.getId());
    }
}
