package io.github.chitchat.server.database.service;

import io.github.chitchat.common.storage.database.service.channel.ServiceChannel;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannelListener;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import io.github.chitchat.server.database.dao.ServerUserDAO;
import io.github.chitchat.server.database.dao.ServerUserDAOImpl;
import io.github.chitchat.server.database.models.ServerUser;
import java.util.Optional;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class ServerUserService extends CacheableIndexableService<ServerUserDAO, ServerUser>
        implements ServiceChannelListener<ServerUser> {
    public ServerUserService(Jdbi db, int cacheSize) {
        super(new ServerUserDAOImpl.OnDemand(db), cacheSize);
    }

    public ServerUserService(
            Jdbi db, int cacheSize, @NotNull ServiceChannel<ServerUser> userServiceChannel) {
        this(db, cacheSize);
        userServiceChannel.register(this);
    }

    public Optional<ServerUser> getByName(String name) {
        return dao.getByName(name);
    }

    public Optional<ServerUser> getByEmail(String email) {
        return dao.getByEmail(email);
    }

    @Override
    public void receiveUpdate(ServerUser model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void receiveRemove(ServerUser model) {
        cache.invalidate(model.getId());
    }
}
