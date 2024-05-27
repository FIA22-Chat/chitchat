package database.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import database.Common;
import database.service.common.IndexableServiceTests;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.service.GroupService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.time.Instant;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class GroupServiceTests extends IndexableServiceTests<GroupService, Group> {
    private static final int CACHE_SIZE = 10;
    private final GroupService groupService = initService("groupServiceTests.db");

    @Override
    protected @NotNull GroupService initService(String name) {
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
        return new GroupService(db, CACHE_SIZE);
    }

    @Override
    @Contract(" -> new")
    protected @NotNull Group generateModel() {
        return new Group(DbUtil.newId(), "TestGroup", "TestDescription", Instant.now());
    }

    @Test
    void getByName() throws DuplicateItemException {
        var group = generateModel();
        groupService.create(group);

        groupService
                .get(group.getName())
                .ifPresentOrElse(u -> assertEquals(group, u), () -> fail("Group not found"));
    }
}
