package database.dao;

import static org.junit.jupiter.api.Assertions.*;

import io.github.chitchat.common.storage.database.Generator;
import io.github.chitchat.common.storage.database.dao.RoleDAO;
import io.github.chitchat.common.storage.database.models.Role;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class RoleTests {
    static final Jdbi db = initDB("role.db");

    static @NotNull Jdbi initDB(String name) {
        var db = Common.createDB(name);
        db.useHandle(
                handle ->
                        handle.execute(
                                """
                                        CREATE TABLE IF NOT EXISTS role
                                        (
                                            id          blob NOT NULL PRIMARY KEY,
                                            group_id    blob NOT NULL REFERENCES "group" (id),
                                            name        text    NOT NULL,
                                            permission  integer NOT NULL,
                                            modified_at text NOT NULL
                                        );
                                        """));
        return db;
    }

    @Contract(" -> new")
    static @NotNull Role generateRole() {
        return new Role(
                Generator.newId(),
                Generator.newId(),
                "TestRole",
                EnumSet.of(PermissionType.SEND_MESSAGE),
                Instant.now());
    }

    @Test
    void testRoleCount() {
        Jdbi dbCount = initDB("testRoleCount.db");
        var dao = dbCount.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);
        assertEquals(1, dao.count());

        dao.delete(role);
        assertEquals(0, dao.count());
    }

    @Test
    void testRoleExistsById() {
        var dao = db.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);
        assertTrue(dao.existsById(role.getId()));

        dao.delete(role);
        assertFalse(dao.existsById(role.getId()));
    }

    @Test
    void testRoleExists() {
        var dao = db.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);
        assertTrue(dao.exists(role));

        dao.delete(role);
        assertFalse(dao.exists(role));
    }

    @Test
    void testRoleGetAll() {
        Jdbi dbGetAll = initDB("roleGetAll.db");
        var dao = dbGetAll.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);
        assertEquals(1, dao.getAll().size());

        dao.delete(role);
    }

    @Test
    void testRoleGetByIds() {
        var dao = db.onDemand(RoleDAO.class);
        var role1 = generateRole();
        var role2 = generateRole();
        dao.insert(role1);
        dao.insert(role2);

        var roles = dao.getByIds(List.of(role1.getId(), role2.getId()));
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    @Test
    void testRoleGetByGroupId() {
        var dao = db.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);

        var roles = dao.getByGroupId(role.getGroupId());
        assertTrue(roles.contains(role));
    }

    @Test
    void testRoleGetByName() {
        var dao = db.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);
        assertEquals(role, dao.getByName(role.getName()).get());

        dao.delete(role);
        assertTrue(dao.getByName(role.getName()).isEmpty());
    }

    @Test
    void testRoleInsert() {
        var dao = db.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);

        var res = dao.getById(role.getId());
        assertTrue(res.isPresent());
        assertEquals(role, res.get());
    }

    @Test
    void testRoleDelete() {
        var dao = db.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);

        var res = dao.getById(role.getId());
        assertTrue(res.isPresent());
        assertEquals(role, res.get());

        dao.delete(role);
        assertTrue(dao.getById(role.getId()).isEmpty());
    }

    @Test
    void testRoleUpdate() {
        var dao = db.onDemand(RoleDAO.class);
        var role = generateRole();
        dao.insert(role);

        var res = dao.getById(role.getId());
        assertTrue(res.isPresent());
        assertEquals(role, res.get());

        var newRole =
                new Role(
                        role.getId(),
                        role.getGroupId(),
                        "NewRole",
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        Instant.now());
        dao.update(newRole);

        res = dao.getById(role.getId());
        assertTrue(res.isPresent());
        assertEquals(newRole, res.get());
    }
}
