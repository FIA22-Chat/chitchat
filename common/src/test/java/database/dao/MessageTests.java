package database.dao;

import static org.junit.jupiter.api.Assertions.*;

import database.Common;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.dao.MessageDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.inner.MessageType;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import java.time.Instant;
import java.util.EnumSet;
import java.util.List;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class MessageTests {
    static final Jdbi db = initDB("message.db");

    static @NotNull Jdbi initDB(String name) {
        var db = Common.createDB(name);
        db.useHandle(
                handle ->
                        handle.execute(
                                """
                                         CREATE TABLE IF NOT EXISTS message
                                         (
                                             id          blob NOT NULL UNIQUE PRIMARY KEY,
                                             user_id     blob NOT NULL REFERENCES user (id),
                                             group_id    blob    NOT NULL REFERENCES "group" (id),
                                             type        integer NOT NULL,
                                             content     blob    NOT NULL,
                                             modified_at text NOT NULL
                                         );
                                        """));
        return db;
    }

    @Contract(" -> new")
    static @NotNull Message generateMessage() {
        return new Message(
                DbUtil.newId(),
                DbUtil.newId(),
                DbUtil.newId(),
                MessageType.TEXT,
                "TestMessage".getBytes(),
                Instant.now());
    }

    @Test
    void testMessageCount() {
        Jdbi dbCount = initDB("testMessageCount.db");
        var dao = new MessageDAOImpl.OnDemand(dbCount);
        var message = generateMessage();
        dao.insert(message);
        assertEquals(1, dao.count());

        dao.delete(message);
        assertEquals(0, dao.count());
    }

    @Test
    void testMessageUserCount() {
        Jdbi dbCount = initDB("testMessageUserCount.db");
        var dao = new MessageDAOImpl.OnDemand(dbCount);
        var message = generateMessage();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        dao.insert(message);
        assertEquals(1, dao.count(user));

        dao.delete(message);
        assertEquals(0, dao.count(user));
    }

    @Test
    void testMessageGroupCount() {
        Jdbi dbCount = initDB("testMessageGroupCount.db");
        var dao = new MessageDAOImpl.OnDemand(dbCount);
        var message = generateMessage();
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());
        dao.insert(message);
        assertEquals(1, dao.count(group));

        dao.delete(message);
        assertEquals(0, dao.count(group));
    }

    @Test
    void testMessageGroupUserCount() {
        Jdbi dbCount = initDB("testMessageGroupUserCount.db");
        var dao = new MessageDAOImpl.OnDemand(dbCount);
        var message = generateMessage();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());
        dao.insert(message);
        assertEquals(1, dao.count(group, user));

        dao.delete(message);
        assertEquals(0, dao.count(group, user));
    }

    @Test
    void testMessageExistsById() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);
        assertTrue(dao.existsById(message.getId()));

        dao.delete(message);
        assertFalse(dao.existsById(message.getId()));
    }

    @Test
    void testMessageExists() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);
        assertTrue(dao.exists(message));

        dao.delete(message);
        assertFalse(dao.exists(message));
    }

    @Test
    void testMessageGetAll() {
        Jdbi dbAll = initDB("testMessageGetAll.db");
        var dao = new MessageDAOImpl.OnDemand(dbAll);
        var message = generateMessage();
        dao.insert(message);
        assertEquals(1, dao.getAll().size());

        dao.delete(message);
        assertEquals(0, dao.getAll().size());
    }

    @Test
    void testMessageGetByIds() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message1 = generateMessage();
        var message2 = generateMessage();
        dao.insert(message1);
        dao.insert(message2);

        var messages = dao.getById(List.of(message1.getId(), message2.getId()));
        assertTrue(messages.contains(message1));
        assertTrue(messages.contains(message2));
    }

    @Test
    void testMessageGetById() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);
        assertEquals(message, dao.getById(message.getId()).get());

        dao.delete(message);
        assertTrue(dao.getById(message.getId()).isEmpty());
    }

    @Test
    void testMessageGetByUser() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        dao.insert(message);
        assertTrue(dao.getByUser(message.getUserId()).contains(message));
        assertTrue(dao.getByUser(user).contains(message));

        dao.delete(message);
        assertTrue(dao.getByUser(message.getUserId()).isEmpty());
        assertTrue(dao.getByUser(user).isEmpty());
    }

    @Test
    void testMessageGetByGroupUser() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());

        dao.insert(message);
        assertTrue(dao.getByGroupUser(group.getId(), user.getId()).contains(message));
        assertTrue(dao.getByGroupUser(group, user).contains(message));

        dao.delete(message);
        assertTrue(dao.getByGroupUser(group.getId(), user.getId()).isEmpty());
        assertTrue(dao.getByGroupUser(group, user).isEmpty());
    }

    @Test
    void testMessageGetByGroup() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());
        dao.insert(message);
        assertTrue(dao.getByGroup(message.getGroupId()).contains(message));
        assertTrue(dao.getByGroup(group).contains(message));

        dao.delete(message);
        assertTrue(dao.getByGroup(message.getGroupId()).isEmpty());
        assertTrue(dao.getByGroup(group).isEmpty());
    }

    @Test
    void testMessageInsert() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);

        var res = dao.getById(message.getId());
        assertTrue(res.isPresent());
        assertEquals(message, res.get());
    }

    @Test
    void testMessageDelete() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);

        var res = dao.getById(message.getId());
        assertTrue(res.isPresent());
        assertEquals(message, res.get());

        dao.delete(message);
        assertTrue(dao.getById(message.getId()).isEmpty());
    }

    @Test
    void testMessageUpdate() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);

        var res = dao.getById(message.getId());
        assertTrue(res.isPresent());
        assertEquals(message, res.get());

        message =
                new Message(
                        message.getId(),
                        message.getUserId(),
                        message.getGroupId(),
                        MessageType.MEDIA,
                        "TestMessage".getBytes(),
                        Instant.now());
        dao.update(message);
    }
}
