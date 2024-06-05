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

    public int count(Group group) {
        return dao.count(group);
    }

    public int count(User user) {
        return dao.count(user);
    }

    public boolean exists(Group group, User user) {
        return dao.exists(group, user);
    }

    public List<UserGroup> getByUser(List<UUID> userIds) {
        return dao.getByUserId(userIds);
    }

    public List<UserGroup> getByUser(UUID userId) {
        return dao.getByUserId(userId);
    }

    public List<UserGroup> getByGroup(List<UUID> group) {
        return dao.getByGroupId(group);
    }

    public List<Group> getUserGroups(UUID userId) {
        return dao.getUserGroups(userId);
    }

    public List<User> getGroupUsers(UUID groupId) {
        return dao.getGroupUsers(groupId);
    }

    public List<Group> getUserGroups(User user) {
        return dao.getUserGroups(user);
    }

    public List<User> getGroupUsers(Group group) {
        return dao.getGroupUsers(group);
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
