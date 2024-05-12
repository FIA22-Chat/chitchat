package io.github.chitchat.common.storage.database.dao.mappers;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.models.inner.MessageType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jetbrains.annotations.NotNull;

public class MessageRowMapper implements RowMapper<Message> {

    @Override
    public Message map(@NotNull ResultSet rs, StatementContext ctx) throws SQLException {
        return new Message(
                UUIDUtil.uuid(rs.getString("id")),
                UUIDUtil.uuid(rs.getString("user_id")),
                UUIDUtil.uuid(rs.getString("group_id")),
                MessageType.from(rs.getInt("type")),
                rs.getBytes("content"),
                Instant.ofEpochMilli(rs.getLong("modified_at")));
    }
}
