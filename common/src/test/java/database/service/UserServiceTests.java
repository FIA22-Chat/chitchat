package database.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import database.Common;
import database.service.common.IndexableServiceTests;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import io.github.chitchat.common.storage.database.service.UserService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.util.EnumSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class UserServiceTests extends IndexableServiceTests<UserService, User> {
    private static final int CACHE_SIZE = 10;
    private final UserService userService = initService("userServiceTests.db");

    @Override
    protected @NotNull UserService initService(String name) {
        var db = Common.createDB(name);
        db.useHandle(
                handle ->
                        handle.execute(
                                """
                                        CREATE TABLE IF NOT EXISTS user ( id         blob NOT NULL UNIQUE
                                        PRIMARY KEY,    type       integer NOT NULL,    permission
                                        integer NOT NULL,    name       text    NOT NULL)
                                        """));
        return new UserService(db, CACHE_SIZE);
    }

    @Override
    @Contract(" -> new")
    protected @NotNull User generateModel() {
        return new User(
                DbUtil.newId(), UserType.USER, EnumSet.of(PermissionType.SEND_MESSAGE), "TestUser");
    }

    @Test
    void getByName() throws DuplicateItemException {
        var user = generateModel();
        userService.create(user);

        userService
                .get(user.getName())
                .ifPresentOrElse(u -> assertEquals(user, u), () -> fail("User not found"));
    }
}
