package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.UserGroupDAO;
import io.github.chitchat.common.storage.database.dao.UserGroupDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.UserGroup;
import io.github.chitchat.common.storage.database.service.common.BaseService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class UserGroupService extends BaseService<UserGroupDAO, UserGroup> {

    public UserGroupService(Jdbi db) {
        super(new UserGroupDAOImpl.OnDemand(db));
    }

    public List<UserGroup> getByUserId(UUID userId) {
        return dao.getByUserId(userId);
    }

    @Override
    public void create(@NotNull UserGroup value) throws DuplicateItemException {
        dao.insert(value);
    }

    @Override
    public void update(@NotNull UserGroup value) {
        dao.update(value);
    }

    @Override
    public void delete(@NotNull UserGroup value) {
        dao.delete(value);
    }

    public void create(Group group, User user, Instant modifiedAt) throws DuplicateItemException {
        dao.insert(group, user, modifiedAt);
    }

    public void update(Group group, User user, Instant modifiedAt) {
        dao.update(group, user, modifiedAt);
    }

    public void delete(Group group, User user) {
        dao.delete(group, user);
    }
}
