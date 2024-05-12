package io.github.chitchat.server.database.dao.mappers;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import io.github.chitchat.common.storage.database.types.BitFlag;
import io.github.chitchat.server.database.models.ServerUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

public class ServerUserRowMapper implements RowMapper<ServerUser> {

    @Override
    public ServerUser map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new ServerUser(
                UUIDUtil.uuid(rs.getBytes("id")),
                UserType.from(rs.getInt("type")),
                BitFlag.fromBitMask(PermissionType.class, rs.getLong("permission")),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                Instant.ofEpochMilli(rs.getLong("modified_at")));
    }
}
