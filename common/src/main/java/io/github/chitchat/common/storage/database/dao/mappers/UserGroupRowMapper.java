package io.github.chitchat.common.storage.database.dao.mappers;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.github.chitchat.common.storage.database.models.UserGroup;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jetbrains.annotations.NotNull;

public class UserGroupRowMapper implements RowMapper<UserGroup> {

    @Override
    public UserGroup map(@NotNull ResultSet rs, StatementContext ctx) throws SQLException {
        return new UserGroup(
                UUIDUtil.uuid(rs.getBytes("user_id")),
                UUIDUtil.uuid(rs.getBytes("group_id")),
                Instant.parse(rs.getString("modified_at")));
    }
}
