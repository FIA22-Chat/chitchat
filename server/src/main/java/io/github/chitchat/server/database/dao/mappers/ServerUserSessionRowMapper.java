package io.github.chitchat.server.database.dao.mappers;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.github.chitchat.server.database.models.ServerUserSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class ServerUserSessionRowMapper implements RowMapper<ServerUserSession> {

    @Override
    public ServerUserSession map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new ServerUserSession(
                UUIDUtil.uuid(rs.getBytes("user_id")),
                rs.getString("token"),
                Instant.parse(rs.getString("expires_at")));
    }
}
