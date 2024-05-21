package database.dao;

import static org.junit.jupiter.api.Assertions.*;

import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.dao.MessageDAOImpl;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.models.inner.MessageType;
import java.time.Instant;
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
    void testMessageGetByUserId() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);
        assertTrue(dao.getByUserId(message.getUserId()).contains(message));

        dao.delete(message);
        assertTrue(dao.getByUserId(message.getUserId()).isEmpty());
    }

    @Test
    void testMessageGetByGroupId() {
        var dao = new MessageDAOImpl.OnDemand(db);
        var message = generateMessage();
        dao.insert(message);
        assertTrue(dao.getByGroupId(message.getGroupId()).contains(message));

        dao.delete(message);
        assertTrue(dao.getByGroupId(message.getGroupId()).isEmpty());
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
