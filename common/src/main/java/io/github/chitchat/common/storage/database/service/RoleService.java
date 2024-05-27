package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.RoleDAO;
import io.github.chitchat.common.storage.database.dao.RoleDAOImpl;
import io.github.chitchat.common.storage.database.models.Role;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannel;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannelListener;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class RoleService extends CacheableIndexableService<RoleDAO, Role>
        implements ServiceChannelListener<Role> {

    public RoleService(Jdbi db, int cacheSize) {
        super(new RoleDAOImpl.OnDemand(db), cacheSize);
    }

    public RoleService(Jdbi db, int cacheSize, @NotNull ServiceChannel<Role> roleServiceChannel) {
        this(db, cacheSize);
        roleServiceChannel.register(this);
    }

    @Override
    public void receiveUpdate(Role model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void receiveRemove(@NotNull Role model) {
        cache.invalidate(model.getId());
    }
}
