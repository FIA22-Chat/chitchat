package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.GroupDAO;
import io.github.chitchat.common.storage.database.dao.GroupDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannel;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannelListener;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class GroupService extends CacheableIndexableService<GroupDAO, Group>
        implements ServiceChannelListener<Group> {

    public GroupService(Jdbi db, int cacheSize) {
        super(new GroupDAOImpl.OnDemand(db), cacheSize);
    }

    public GroupService(
            Jdbi db, int cacheSize, @NotNull ServiceChannel<Group> groupServiceChannel) {
        this(db, cacheSize);
        groupServiceChannel.register(this);
    }

    @Override
    public void receiveUpdate(Group model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void receiveRemove(@NotNull Group model) {
        cache.invalidate(model.getId());
    }
}
