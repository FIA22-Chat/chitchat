package database.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import database.Common;
import database.service.common.IndexableServiceTests;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.inner.MessageType;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import io.github.chitchat.common.storage.database.service.MessageService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.time.Instant;
import java.util.EnumSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class MessageServiceTests extends IndexableServiceTests<MessageService, Message> {
    private static final int CACHE_SIZE = 10;
    private final MessageService messageService = initService("messageServiceTests.db");

    @Override
    protected @NotNull MessageService initService(String name) {
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
        return new MessageService(db, CACHE_SIZE);
    }

    @Override
    @Contract(" -> new")
    protected @NotNull Message generateModel() {
        return new Message(
                DbUtil.newId(),
                DbUtil.newId(),
                DbUtil.newId(),
                MessageType.TEXT,
                "TestMessage".getBytes(),
                Instant.now());
    }

    @Test
    void countByUser() throws DuplicateItemException {
        var message = generateModel();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        messageService.create(message);
        assertEquals(1, messageService.count(user));

        messageService.delete(message);
        assertEquals(0, messageService.count(user));
    }

    @Test
    void countByGroup() throws DuplicateItemException {
        var message = generateModel();
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());
        messageService.create(message);
        assertEquals(1, messageService.count(group));

        messageService.delete(message);
        assertEquals(0, messageService.count(group));
    }

    @Test
    void countByGroupUser() throws DuplicateItemException {
        var message = generateModel();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());
        messageService.create(message);
        assertEquals(1, messageService.count(group, user));

        messageService.delete(message);
        assertEquals(0, messageService.count(group, user));
    }

    @Test
    void getByUser() throws DuplicateItemException {
        var message = generateModel();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        messageService.create(message);
        assertEquals(1, messageService.getByUser(user).size());

        messageService.delete(message);
        assertEquals(0, messageService.getByUser(user).size());
    }

    @Test
    void getByGroup() throws DuplicateItemException {
        var message = generateModel();
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());
        messageService.create(message);
        assertEquals(1, messageService.getByGroup(group).size());

        messageService.delete(message);
        assertEquals(0, messageService.getByGroup(group).size());
    }

    @Test
    void getByGroupUser() throws DuplicateItemException {
        var message = generateModel();
        var user =
                new User(
                        message.getUserId(),
                        UserType.USER,
                        EnumSet.of(PermissionType.SEND_MESSAGE),
                        "TestUser");
        var group = new Group(message.getGroupId(), "TestGroup", "TestDescription", Instant.now());
        messageService.create(message);
        assertEquals(1, messageService.getByGroupUser(group, user).size());

        messageService.delete(message);
        assertEquals(0, messageService.getByGroupUser(group, user).size());
    }
}
