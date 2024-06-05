package database.dao;

import static org.junit.jupiter.api.Assertions.*;

import database.Common;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.dao.*;
import io.github.chitchat.common.storage.database.models.*;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class UserRoleTests {
    static final Jdbi db = initDB("userRole.db");

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
                                    CREATE TABLE IF NOT EXISTS role
                                    (
                                        id          blob    NOT NULL PRIMARY KEY,
                                        group_id    blob    NOT NULL REFERENCES "group" (id),
                                        name        text    NOT NULL,
                                        permission  integer NOT NULL,
                                        modified_at text    NOT NULL
                                    );
                                    """);
                    handle.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS user_role
                                    (
                                        user_id     blob NOT NULL REFERENCES user (id),
                                        role_id     blob NOT NULL REFERENCES role (id),
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

    static @NotNull Role generateRole(UUID groupId) {
        return new Role(
                DbUtil.newId(),
                groupId,
                "TestRole",
                EnumSet.of(PermissionType.SEND_MESSAGE),
                Instant.now());
    }

    static @NotNull UserRole generateUserRole(UUID userId, UUID roleId) {
        return new UserRole(userId, roleId, Instant.now());
    }

    @Test
    void testUserRoleCount() {
        Jdbi dbCount = initDB("testUserRoleCount.db");
        var userDao = new UserDAOImpl.OnDemand(dbCount);
        var groupDao = new GroupDAOImpl.OnDemand(dbCount);
        var roleDao = new RoleDAOImpl.OnDemand(dbCount);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(dbCount);

        var user = generateUser();
        var group = generateGroup();
        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());

        userDao.insert(user);
        groupDao.insert(group);
        roleDao.insert(role);
        userRoleDao.insert(userRole);
        assertEquals(1, userRoleDao.count());
        assertEquals(1, userRoleDao.count(user));
        assertEquals(1, userRoleDao.count(user, group));

        userRoleDao.delete(userRole);
        assertEquals(0, userRoleDao.count());
        assertEquals(0, userRoleDao.count(user));
        assertEquals(0, userRoleDao.count(user, group));
    }

    @Test
    void testUserRoleExists() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var roleDao = new RoleDAOImpl.OnDemand(db);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();
        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());

        userDao.insert(user);
        groupDao.insert(group);
        roleDao.insert(role);
        userRoleDao.insert(userRole);

        assertTrue(userRoleDao.exists(user, role));
        assertTrue(userRoleDao.exists(user.getId(), role.getId()));
        assertTrue(userRoleDao.exists(user, role));
        assertTrue(userRoleDao.exists(userRole));
    }

    @Test
    void testUserRoleGetAll() {
        Jdbi dbAll = initDB("testUserRoleGetAll.db");
        var userDao = new UserDAOImpl.OnDemand(dbAll);
        var groupDao = new GroupDAOImpl.OnDemand(dbAll);
        var roleDao = new RoleDAOImpl.OnDemand(dbAll);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(dbAll);

        var user = generateUser();
        var group = generateGroup();

        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());
        var role2 = generateRole(group.getId());
        var userRole2 = generateUserRole(user.getId(), role2.getId());

        userDao.insert(user);
        groupDao.insert(group);

        roleDao.insert(role);
        userRoleDao.insert(userRole);
        roleDao.insert(role2);
        userRoleDao.insert(userRole2);

        var userRoles = userRoleDao.getAll();
        assertEquals(2, userRoles.size());
        assertTrue(userRoles.contains(userRole));
        assertTrue(userRoles.contains(userRole2));
    }

    @Test
    void testUserRoleGetByUser() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var roleDao = new RoleDAOImpl.OnDemand(db);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();
        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());

        userDao.insert(user);
        groupDao.insert(group);
        roleDao.insert(role);
        userRoleDao.insert(userRole);

        var userRoles = userRoleDao.getUserRoles(user);
        assertEquals(1, userRoles.size());
        assertEquals(role, userRoles.getFirst());
    }

    @Test
    void testUserRoleGetByUsers() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var roleDao = new RoleDAOImpl.OnDemand(db);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();

        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());
        var role2 = generateRole(group.getId());
        var userRole2 = generateUserRole(user.getId(), role2.getId());

        userDao.insert(user);
        groupDao.insert(group);

        roleDao.insert(role);
        userRoleDao.insert(userRole);
        roleDao.insert(role2);
        userRoleDao.insert(userRole2);

        var userRoles = userRoleDao.getUserRoles(List.of(user.getId()));
        assertEquals(2, userRoles.size());
        assertTrue(userRoles.contains(role));
        assertTrue(userRoles.contains(role2));

        userRoles = userRoleDao.getUserRoles(List.of(user.getId()));
        assertEquals(2, userRoles.size());
        assertTrue(userRoles.contains(role));
        assertTrue(userRoles.contains(role2));
    }

    @Test
    void testUserRoleGetByGroup() {
        var userDao = new UserDAOImpl.OnDemand(db);
        var groupDao = new GroupDAOImpl.OnDemand(db);
        var roleDao = new RoleDAOImpl.OnDemand(db);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(db);

        var user = generateUser();
        var group = generateGroup();
        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());

        userDao.insert(user);
        groupDao.insert(group);
        roleDao.insert(role);
        userRoleDao.insert(userRole);

        var userRoles = userRoleDao.getGroupUserRoles(group.getId(), user.getId());
        assertEquals(1, userRoles.size());
        assertEquals(role, userRoles.getFirst());

        userRoles = userRoleDao.getGroupUserRoles(group, user);
        assertEquals(1, userRoles.size());
        assertEquals(role, userRoles.getFirst());

        userRoleDao.delete(userRole);
        userRoles = userRoleDao.getGroupUserRoles(group.getId(), user.getId());
        assertEquals(0, userRoles.size());

        userRoles = userRoleDao.getGroupUserRoles(group, user);
        assertEquals(0, userRoles.size());
    }

    @Test
    void testUserRoleInsert() {
        var dbInsert = initDB("testUserRoleInsert.db");
        var userDao = new UserDAOImpl.OnDemand(dbInsert);
        var groupDao = new GroupDAOImpl.OnDemand(dbInsert);
        var roleDao = new RoleDAOImpl.OnDemand(dbInsert);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(dbInsert);

        var user = generateUser();
        var group = generateGroup();
        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());

        userDao.insert(user);
        groupDao.insert(group);
        roleDao.insert(role);
        userRoleDao.insert(userRole);

        var userRoles = userRoleDao.getAll();
        assertEquals(1, userRoles.size());
        assertEquals(userRole, userRoles.getFirst());
    }

    @Test
    void testUserRoleDelete() {
        var dbDelete = initDB("testUserRoleDelete.db");
        var userDao = new UserDAOImpl.OnDemand(dbDelete);
        var groupDao = new GroupDAOImpl.OnDemand(dbDelete);
        var roleDao = new RoleDAOImpl.OnDemand(dbDelete);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(dbDelete);

        var user = generateUser();
        var group = generateGroup();
        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());

        userDao.insert(user);
        groupDao.insert(group);
        roleDao.insert(role);
        userRoleDao.insert(userRole);

        var userRoles = userRoleDao.getAll();
        assertEquals(1, userRoles.size());
        assertEquals(userRole, userRoles.getFirst());

        userRoleDao.delete(userRole);
        userRoles = userRoleDao.getAll();
        assertEquals(0, userRoles.size());
    }

    @Test
    void testUserRoleUpdate() {
        var dbUpdate = initDB("testUserRoleUpdate.db");
        var userDao = new UserDAOImpl.OnDemand(dbUpdate);
        var groupDao = new GroupDAOImpl.OnDemand(dbUpdate);
        var roleDao = new RoleDAOImpl.OnDemand(dbUpdate);
        var userRoleDao = new UserRoleDAOImpl.OnDemand(dbUpdate);

        var user = generateUser();
        var group = generateGroup();
        var role = generateRole(group.getId());
        var userRole = generateUserRole(user.getId(), role.getId());

        userDao.insert(user);
        groupDao.insert(group);
        roleDao.insert(role);
        userRoleDao.insert(userRole);

        var userRoles = userRoleDao.getAll();
        assertEquals(1, userRoles.size());
        assertEquals(userRole, userRoles.getFirst());

        var newRole = generateRole(group.getId());
        userRole.setRoleId(newRole.getId());
        userRole.setModifiedAt(Instant.now());

        roleDao.insert(newRole);
        userRoleDao.update(userRole);

        userRoles = userRoleDao.getAll();
        assertEquals(1, userRoles.size());
        assertEquals(userRole, userRoles.getFirst());
    }
}
