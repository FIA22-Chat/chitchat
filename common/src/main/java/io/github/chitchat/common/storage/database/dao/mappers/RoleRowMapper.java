package io.github.chitchat.common.storage.database.dao.mappers;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.github.chitchat.common.storage.database.models.Role;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.types.BitFlag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jetbrains.annotations.NotNull;

public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role map(@NotNull ResultSet rs, StatementContext ctx) throws SQLException {
        return new Role(
                UUIDUtil.uuid(rs.getString("id")),
                UUIDUtil.uuid(rs.getString("group_id")),
                rs.getString("name"),
                BitFlag.fromBitMask(PermissionType.class, rs.getLong("permission")),
                Instant.ofEpochMilli(rs.getLong("modified_at")));
    }
}