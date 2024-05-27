package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.UserDAO;
import io.github.chitchat.common.storage.database.dao.UserDAOImpl;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannel;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannelListener;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import java.util.Optional;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class UserService extends CacheableIndexableService<UserDAO, User>
        implements ServiceChannelListener<User> {

    public UserService(Jdbi db, int cacheSize) {
        super(new UserDAOImpl.OnDemand(db), cacheSize);
    }

    public UserService(Jdbi db, int cacheSize, @NotNull ServiceChannel<User> userServiceChannel) {
        this(db, cacheSize);
        userServiceChannel.register(this);
    }

    public Optional<User> get(String name) {
        return dao.getByName(name);
    }

    @Override
    public void ReceiveUpdate(User model) {
        cache.put(model.getId(), model);
    }

    @Override
    public void ReceiveRemove(@NotNull User model) {
        cache.invalidate(model.getId());
    }
}
