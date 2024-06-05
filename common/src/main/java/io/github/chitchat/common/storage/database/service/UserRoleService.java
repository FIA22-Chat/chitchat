package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.UserRoleDAO;
import io.github.chitchat.common.storage.database.dao.UserRoleDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.Role;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.UserRole;
import io.github.chitchat.common.storage.database.service.common.BaseService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class UserRoleService extends BaseService<UserRoleDAO, UserRole> {
    public UserRoleService(Jdbi db) {
        super(new UserRoleDAOImpl.OnDemand(db));
    }

    public int count(User user) {
        return dao.count(user);
    }

    public int count(User user, Group group) {
        return dao.count(user, group);
    }

    public boolean exists(UUID userid, UUID roleid) {
        return dao.exists(userid, roleid);
    }

    public boolean exists(User user, Role role) {
        return dao.exists(user, role);
    }

    public boolean exists(UserRole userRole) {
        return dao.exists(userRole);
    }

    public List<Role> getUserRoles(UUID userId) {
        return dao.getUserRoles(userId);
    }

    public List<Role> getUserRoles(User user) {
        return dao.getUserRoles(user);
    }

    public List<Role> getGroupUserRoles(UUID userId, UUID groupId) {
        return dao.getGroupUserRoles(userId, groupId);
    }

    public List<Role> getGroupUserRoles(Group group, User user) {
        return dao.getGroupUserRoles(group, user);
    }

    @Override
    public void create(@NotNull UserRole value) throws DuplicateItemException {
        dao.insert(value);
    }

    @Override
    public void update(@NotNull UserRole value) {
        dao.update(value);
    }

    @Override
    public void delete(@NotNull UserRole value) {
        dao.delete(value);
    }

    public void create(User user, Role role, Instant modifiedAt) {
        dao.insert(user, role, modifiedAt);
    }

    public void delete(User user, Role role) {
        dao.delete(user, role);
    }

    public void update(User user, Role role, Instant modifiedAt) {
        dao.update(user, role, modifiedAt);
    }
}
