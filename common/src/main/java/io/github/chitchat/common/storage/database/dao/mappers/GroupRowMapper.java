package io.github.chitchat.common.storage.database.dao.mappers;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.github.chitchat.common.storage.database.models.Group;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jetbrains.annotations.NotNull;

public class GroupRowMapper implements RowMapper<Group> {

    @Override
    public Group map(@NotNull ResultSet rs, StatementContext ctx) throws SQLException {
        return new Group(
                UUIDUtil.uuid(rs.getString("id")),
                rs.getString("name"),
                rs.getString("description"),
                Instant.ofEpochMilli(rs.getLong("modified_at")));
    }
}
