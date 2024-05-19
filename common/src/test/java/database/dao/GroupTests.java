package database.dao;

import static org.junit.jupiter.api.Assertions.*;

import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.dao.GroupDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import java.time.Instant;
import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class GroupTests {
    static final Jdbi db = initDB("group.db");

    static @NotNull Jdbi initDB(String name) {
        var db = Common.createDB(name);
        db.useHandle(
                handle ->
                        handle.execute(
                                "CREATE TABLE IF NOT EXISTS \"group\""
                                        + "("
                                        + "    id          blob NOT NULL UNIQUE PRIMARY KEY,"
                                        + "    name        text    NOT NULL,"
                                        + "    description text    NOT NULL,"
                                        + "    modified_at text NOT NULL"
                                        + ");"));
        return db;
    }

    @Contract(" -> new")
    static @NotNull Group generateGroup() {
        return new Group(DbUtil.newId(), "TestGroup", "TestDescription", Instant.now());
    }

    @Test
    void testGroupCount() {
        Jdbi dbCount = initDB("testCount.db");
        var dao = new GroupDAOImpl.OnDemand(dbCount);
        var group = generateGroup();
        dao.insert(group);
        assertEquals(1, dao.count());

        dao.delete(group);
        assertEquals(0, dao.count());
    }

    @Test
    void testGroupExistsById() {
        var dao = new GroupDAOImpl.OnDemand(db);
        var group = generateGroup();
        dao.insert(group);
        assertTrue(dao.existsById(group.getId()));

        dao.delete(group);
        assertFalse(dao.existsById(group.getId()));
    }

    @Test
    void testGroupExists() {
        var dao = new GroupDAOImpl.OnDemand(db);
        var group = generateGroup();
        dao.insert(group);
        assertTrue(dao.exists(group));

        dao.delete(group);
        assertFalse(dao.exists(group));
    }

    @Test
    void testGroupGetAll() {
        Jdbi dbGetAll = initDB("groupGetAll.db");
        var dao = new GroupDAOImpl.OnDemand(dbGetAll);
        var group = generateGroup();
        dao.insert(group);
        assertEquals(1, dao.getAll().size());

        dao.delete(group);
        assertEquals(0, dao.getAll().size());
    }

    @Test
    void testGroupGetByIds() {
        var dao = new GroupDAOImpl.OnDemand(db);
        var group1 = generateGroup();
        var group2 = generateGroup();
        dao.insert(group1);
        dao.insert(group2);

        var list = dao.getById(List.of(group1.getId(), group2.getId()));
        assertTrue(list.contains(group1));
        assertTrue(list.contains(group2));
    }

    @Test
    void testGroupGetById() {
        var dao = new GroupDAOImpl.OnDemand(db);
        var group = generateGroup();
        dao.insert(group);
        assertEquals(group, dao.getById(group.getId()).get());

        dao.delete(group);
        assertTrue(dao.getById(group.getId()).isEmpty());
    }

    @Test
    void testGroupGetByName() {
        var dbName = initDB("groupGetByName.db");
        var dao = new GroupDAOImpl.OnDemand(dbName);
        var group = generateGroup();
        dao.insert(group);
        assertEquals(group, dao.getByName(group.getName()).get());

        dao.delete(group);
        assertTrue(dao.getByName(group.getName()).isEmpty());
    }

    @Test
    void testGroupInsert() {
        var dao = new GroupDAOImpl.OnDemand(db);
        var group = generateGroup();
        dao.insert(group);

        var res = dao.getById(group.getId());
        assertTrue(res.isPresent());
        assertEquals(group, res.get());
    }

    @Test
    void testGroupDelete() {
        var dao = new GroupDAOImpl.OnDemand(db);
        var group = generateGroup();
        dao.insert(group);

        var res = dao.getById(group.getId());
        assertTrue(res.isPresent());
        assertEquals(group, res.get());

        dao.delete(group);
        assertTrue(dao.getById(group.getId()).isEmpty());
    }

    @Test
    void testGroupUpdate() {
        var dao = new GroupDAOImpl.OnDemand(db);
        var group = generateGroup();
        dao.insert(group);

        var res = dao.getById(group.getId());
        assertTrue(res.isPresent());
        assertEquals(group, res.get());

        var updatedGroup =
                new Group(group.getId(), "UpdatedName", "UpdatedDescription", Instant.now());
        dao.update(updatedGroup);

        res = dao.getById(group.getId());
        assertTrue(res.isPresent());
        assertEquals(updatedGroup, res.get());
    }
}
