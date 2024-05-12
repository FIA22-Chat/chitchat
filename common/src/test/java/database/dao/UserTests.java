package database.dao;

import static org.junit.jupiter.api.Assertions.*;

import io.github.chitchat.common.storage.database.Database;
import io.github.chitchat.common.storage.database.Generator;
import io.github.chitchat.common.storage.database.dao.UserDAO;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteDataSource;

public class UserTests {
    static final Path PATH = Path.of("src/test/resources").toAbsolutePath();
    static final Jdbi db = createDB("test.db");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static @NotNull Jdbi createDB(String name) {
        var dbPath = PATH.resolve(name);
        dbPath.toFile().delete();
        dbPath.toFile().deleteOnExit();

        var datasource = new SQLiteDataSource();
        datasource.setUrl("jdbc:sqlite:" + dbPath);

        var db = Database.create(datasource, false, new SQLitePlugin());
        db.useHandle(
                handle ->
                        handle.execute(
                                "CREATE TABLE IF NOT EXISTS user ( id         blob NOT NULL UNIQUE"
                                    + " PRIMARY KEY,    type       integer NOT NULL,    permission"
                                    + " integer NOT NULL,    name       text    NOT NULL)"));
        return db;
    }

    @Contract(" -> new")
    static @NotNull User generateUser() {
        return new User(
                Generator.newId(),
                UserType.USER,
                EnumSet.of(PermissionType.SEND_MESSAGE),
                "TestUser");
    }

    @Test
    void testUserCount() {
        Jdbi dbCount = createDB("testCount.db");
        var dao = dbCount.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);
        assertEquals(1, dao.count());

        dao.delete(user);
        assertEquals(0, dao.count());
    }

    @Test
    void testUserExistsById() {
        var dao = db.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);
        assertTrue(dao.existsById(user.getId()));

        dao.delete(user);
        assertFalse(dao.existsById(user.getId()));
    }

    @Test
    void testUserExists() {
        var dao = db.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);
        assertTrue(dao.exists(user));

        dao.delete(user);
        assertFalse(dao.exists(user));
    }

    @Test
    void testUserGetAll() {
        Jdbi dbAll = createDB("testAll.db");
        var dao = dbAll.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);
        assertEquals(1, dao.getAll().size());

        dao.delete(user);
        assertEquals(0, dao.getAll().size());
    }

    @Test
    void testUserGetByIds() {
        var dao = db.onDemand(UserDAO.class);
        var user1 = generateUser();
        var user2 = generateUser();
        dao.insert(user1);
        dao.insert(user2);

        ArrayList<UUID> list = new ArrayList<>();
        list.add(user1.getId());
        list.add(user2.getId());

        var users = dao.getByIds(list);
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testUserGetById() {
        var dao = db.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);
        assertEquals(user, dao.getById(user.getId()).get());

        dao.delete(user);
        assertTrue(dao.getById(user.getId()).isEmpty());
    }

    @Test
    void testUserGetByName() {
        var dbName = createDB("testName.db");
        var dao = dbName.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);
        assertEquals(user, dao.getByName(user.getName()).get());

        dao.delete(user);
        assertTrue(dao.getByName(user.getName()).isEmpty());
    }

    @Test
    void testUserInsert() {
        var dao = db.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);

        var res = dao.getById(user.getId());
        assertTrue(res.isPresent());
        assertEquals(user, res.get());
    }

    @Test
    void testUserDelete() {
        var dao = db.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);

        var res = dao.getById(user.getId());
        assertTrue(res.isPresent());
        assertEquals(user, res.get());

        dao.delete(user);
        assertTrue(dao.getById(user.getId()).isEmpty());
    }

    @Test
    void testUserUpdate() {
        var dao = db.onDemand(UserDAO.class);
        var user = generateUser();
        dao.insert(user);

        var res = dao.getById(user.getId());
        assertTrue(res.isPresent());
        assertEquals(user, res.get());

        user =
                new User(
                        user.getId(),
                        UserType.BOT,
                        EnumSet.of(PermissionType.DELETE_MESSAGE),
                        "TestUser2");
        dao.update(user);

        res = dao.getById(user.getId());
        assertTrue(res.isPresent());
        assertEquals(user, res.get());
    }
}
