package database.dao;

import static org.junit.jupiter.api.Assertions.*;

import database.Common;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.dao.GroupDAOImpl;
import io.github.chitchat.common.storage.database.dao.UserDAOImpl;
import io.github.chitchat.common.storage.database.dao.UserGroupDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.UserGroup;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class UserGroupTests {
    static final Jdbi db = initDB("userGroup.db");

    static @NotNull Jdbi initDB(String name) {
        var db = Common.createDB(name);
        db.useHandle(
                handle -> {
                    handle.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS user
                                    (
                                        id         blob    NOT NULL UNIQUE PRIMARY KEY,
                                        type       integer NOT NULL,
                                        permission integer NOT NULL,
                                        name       text    NOT NULL
                                    );
                                    """);
                    handle.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS "group"
                                    (
                                        id          blob NOT NULL UNIQUE PRIMARY KEY,
                                        name        text NOT NULL,
                                        description text NOT NULL,
                                        modified_at text NOT NULL
                                    );
                                    """);
                    handle.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS user_group
                                    (
                                        user_id     blob NOT NULL REFERENCES user (id),
                                        group_id    blob NOT NULL REFERENCES "group" (id),
                                        modified_at text NOT NULL
                                    );
                                    """);
                });
        return db;
    }

    @Contract(" -> new")
    static @NotNull User generateUser() {
        return new User(
                DbUtil.newId(), UserType.USER, EnumSet.of(PermissionType.SEND_MESSAGE), "TestUser");
    }

    @Contract(" -> new")
    static @NotNull Group generateGroup() {
        return new Group(DbUtil.newId(), "TestGroup", "TestDescription", Instant.now());
    }

    @Test
    void testUserGroupCount() {
        Jdbi dbCount = initDB("testUserGroupCount.db");
        var userDao = new UserDAOImpl.OnDemand(dbCount);
        var groupDao = new GroupDAOImpl.OnDemand(dbCount);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(dbCount);

        var user = generateUser();
        var group = generateGroup();
        userDao.insert(user);
        groupDao.insert(group);
        userGroupDao.insert(group, user, Instant.now());

        assertEquals(1, userGroupDao.count());
        assertEquals(1, userGroupDao.count(group));
        assertEquals(1, userGroupDao.count(user));

        userGroupDao.delete(group, user);
        assertEquals(0, userGroupDao.count());
        assertEquals(0, userGroupDao.count(group));
        assertEquals(0, userGroupDao.count(user));
    }

    @Test
    void testUserGroupExists() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();
        var userGroup = new UserGroup(user, group, Instant.now());
        userDao.insert(user);
        groupDao.insert(group);
        userGroupDao.insert(userGroup);

        assertTrue(userGroupDao.exists(group.getId(), user.getId()));
        assertTrue(userGroupDao.exists(group, user));
        assertTrue(userGroupDao.exists(userGroup));

        userGroupDao.delete(group, user);
        assertFalse(userGroupDao.exists(group.getId(), user.getId()));
        assertFalse(userGroupDao.exists(group, user));
        assertFalse(userGroupDao.exists(userGroup));
    }

    @Test
    void testUserGroupGetAll() {
        Jdbi dbAll = initDB("testUserGroupGetAll.db");
        var userDao = new UserDAOImpl.OnDemand(dbAll);
        var groupDao = new GroupDAOImpl.OnDemand(dbAll);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(dbAll);

        var user = generateUser();
        var group = generateGroup();
        var userGroup = new UserGroup(user, group, Instant.now());
        userDao.insert(user);
        groupDao.insert(group);
        userGroupDao.insert(userGroup);
        assertEquals(1, userGroupDao.getAll().size());

        userGroupDao.delete(group, user);
        assertEquals(0, userGroupDao.getAll().size());
    }

    @Test
    void testUserGroupGetByUserId() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();
        var userGroup = new UserGroup(user, group, Instant.now());
        userDao.insert(user);
        groupDao.insert(group);
        userGroupDao.insert(userGroup);
        assertEquals(1, userGroupDao.getByUserId(user.getId()).size());

        userGroupDao.delete(group, user);
        assertEquals(0, userGroupDao.getByUserId(user.getId()).size());
    }

    @Test
    void testUserGroupGetByUserIds() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user1 = generateUser();
        var user2 = generateUser();
        var group = generateGroup();
        var userGroup1 = new UserGroup(user1, group, Instant.now());
        var userGroup2 = new UserGroup(user2, group, Instant.now());
        userDao.insert(user1);
        userDao.insert(user2);
        groupDao.insert(group);
        userGroupDao.insert(userGroup1);
        userGroupDao.insert(userGroup2);
        assertEquals(2, userGroupDao.getByUserId(List.of(user1.getId(), user2.getId())).size());

        userGroupDao.delete(group, user1);
        userGroupDao.delete(group, user2);
        assertEquals(0, userGroupDao.getByUserId(List.of(user1.getId(), user2.getId())).size());
    }

    @Test
    void testUserGroupGetByGroupId() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();
        var userGroup = new UserGroup(user, group, Instant.now());
        userDao.insert(user);
        groupDao.insert(group);
        userGroupDao.insert(userGroup);
        assertEquals(1, userGroupDao.getByGroupId(group.getId()).size());

        userGroupDao.delete(group, user);
        assertEquals(0, userGroupDao.getByGroupId(group.getId()).size());
    }

    @Test
    void testUserGroupGetByGroupIds() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var group1 = generateGroup();
        var group2 = generateGroup();
        var userGroup1 = new UserGroup(user, group1, Instant.now());
        var userGroup2 = new UserGroup(user, group2, Instant.now());
        userDao.insert(user);
        groupDao.insert(group1);
        groupDao.insert(group2);
        userGroupDao.insert(userGroup1);
        userGroupDao.insert(userGroup2);
        assertEquals(2, userGroupDao.getByGroupId(List.of(group1.getId(), group2.getId())).size());

        userGroupDao.delete(group1, user);
        userGroupDao.delete(group2, user);
        assertEquals(0, userGroupDao.getByGroupId(List.of(group1.getId(), group2.getId())).size());
    }

    @Test
    void testUserGroupGetGroupUsers() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user1 = generateUser();
        var user2 = generateUser();
        var group = generateGroup();
        var userGroup1 = new UserGroup(user1, group, Instant.now());
        var userGroup2 = new UserGroup(user2, group, Instant.now());
        userDao.insert(user1);
        userDao.insert(user2);
        groupDao.insert(group);
        userGroupDao.insert(userGroup1);
        userGroupDao.insert(userGroup2);

        var groupUsers = userGroupDao.getGroupUsers(group.getId());
        assertEquals(2, groupUsers.size());
        assertTrue(groupUsers.contains(user1));
        assertTrue(groupUsers.contains(user2));
        groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(2, groupUsers.size());
        assertTrue(groupUsers.contains(user1));
        assertTrue(groupUsers.contains(user2));

        userGroupDao.delete(group, user1);
        userGroupDao.delete(group, user2);
        groupUsers = userGroupDao.getGroupUsers(group.getId());
        assertEquals(0, groupUsers.size());
        groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(0, groupUsers.size());
    }

    @Test
    void testUserGroupGetUserGroups() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var group1 = generateGroup();
        var group2 = generateGroup();
        var userGroup1 = new UserGroup(user, group1, Instant.now());
        var userGroup2 = new UserGroup(user, group2, Instant.now());
        userDao.insert(user);
        groupDao.insert(group1);
        groupDao.insert(group2);
        userGroupDao.insert(userGroup1);
        userGroupDao.insert(userGroup2);

        var userGroups = userGroupDao.getUserGroups(user.getId());
        assertEquals(2, userGroups.size());
        assertTrue(userGroups.contains(group1));
        assertTrue(userGroups.contains(group2));
        userGroups = userGroupDao.getUserGroups(user);
        assertEquals(2, userGroups.size());
        assertTrue(userGroups.contains(group1));
        assertTrue(userGroups.contains(group2));

        userGroupDao.delete(group1, user);
        userGroupDao.delete(group2, user);
        userGroups = userGroupDao.getUserGroups(user.getId());
        assertEquals(0, userGroups.size());
        userGroups = userGroupDao.getUserGroups(user);
        assertEquals(0, userGroups.size());
    }

    @Test
    void testUserGroupInsert() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var user2 = generateUser();
        var user3 = generateUser();
        var user4 = generateUser();
        var group = generateGroup();
        userDao.insert(user);
        userDao.insert(user2);
        userDao.insert(user3);
        userDao.insert(user4);
        groupDao.insert(group);
        userGroupDao.insert(group, user, Instant.now());
        userGroupDao.insert(new UserGroup(user2, group, Instant.now()));
        userGroupDao.insert(Instant.now(), group, user3, user4);

        var groupUsers = userGroupDao.getGroupUsers(group.getId());
        assertEquals(4, groupUsers.size());
        assertTrue(groupUsers.contains(user));
        assertTrue(groupUsers.contains(user2));

        var userGroups = userGroupDao.getUserGroups(user.getId());
        assertEquals(1, userGroups.size());
        assertTrue(userGroups.contains(group));
        userGroups = userGroupDao.getUserGroups(user2.getId());
        assertEquals(1, userGroups.size());
        assertTrue(userGroups.contains(group));
        userGroups = userGroupDao.getUserGroups(user3.getId());
        assertEquals(1, userGroups.size());
        assertTrue(userGroups.contains(group));
        userGroups = userGroupDao.getUserGroups(user4.getId());
        assertEquals(1, userGroups.size());
        assertTrue(userGroups.contains(group));

        userGroupDao.delete(group, user);
        userGroupDao.delete(group, user2);
        userGroupDao.delete(group, user3);
        userGroupDao.delete(group, user4);
        groupUsers = userGroupDao.getGroupUsers(group.getId());
        assertEquals(0, groupUsers.size());
        userGroups = userGroupDao.getUserGroups(user.getId());
        assertEquals(0, userGroups.size());
        userGroups = userGroupDao.getUserGroups(user2.getId());
        assertEquals(0, userGroups.size());
        userGroups = userGroupDao.getUserGroups(user3.getId());
        assertEquals(0, userGroups.size());
        userGroups = userGroupDao.getUserGroups(user4.getId());
        assertEquals(0, userGroups.size());
    }

    @Test
    void testUserGroupDelete() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var user2 = generateUser();
        var user3 = generateUser();
        var user4 = generateUser();
        var group = generateGroup();
        var userGroup2 = new UserGroup(user2, group, Instant.now());
        var userGroup3 = new UserGroup(user3, group, Instant.now());
        var userGroup4 = new UserGroup(user4, group, Instant.now());
        userDao.insert(user);
        userDao.insert(user2);
        userDao.insert(user3);
        userDao.insert(user4);
        groupDao.insert(group);
        userGroupDao.insert(group, user, Instant.now());
        userGroupDao.insert(userGroup2);
        userGroupDao.insert(userGroup3);
        userGroupDao.insert(userGroup4);

        userGroupDao.delete(group, user);
        var groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(3, groupUsers.size());
        assertFalse(groupUsers.contains(user));
        assertTrue(groupUsers.contains(user2));
        assertTrue(groupUsers.contains(user3));
        assertTrue(groupUsers.contains(user4));

        userGroupDao.delete(userGroup2);
        groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(2, groupUsers.size());
        assertFalse(groupUsers.contains(user));
        assertFalse(groupUsers.contains(user2));
        assertTrue(groupUsers.contains(user3));
        assertTrue(groupUsers.contains(user4));

        userGroupDao.delete(group, List.of(user3.getId(), user4.getId()));
        groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(0, groupUsers.size());
    }

    @Test
    void testUserGroupUpdate() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var userGroupDao = new UserGroupDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();
        userDao.insert(user);
        groupDao.insert(group);
        userGroupDao.insert(group, user, Instant.now());

        var groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(1, groupUsers.size());
        assertTrue(groupUsers.contains(user));

        userGroupDao.update(group, user, Instant.now());
        groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(1, groupUsers.size());
        assertTrue(groupUsers.contains(user));

        userGroupDao.delete(group, user);
        groupUsers = userGroupDao.getGroupUsers(group);
        assertEquals(0, groupUsers.size());
    }
}
