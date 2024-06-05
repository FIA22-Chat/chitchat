package io.github.chitchat.common.storage.database.dao.mappers;

import com.fasterxml.uuid.impl.UUIDUtil;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import io.github.chitchat.common.storage.database.types.BitFlag;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.jetbrains.annotations.NotNull;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User map(@NotNull ResultSet rs, StatementContext ctx) throws SQLException {
        return new User(
                UUIDUtil.uuid(rs.getBytes("id")),
                UserType.from(rs.getInt("type")),
                BitFlag.fromBitMask(PermissionType.class, rs.getLong("permission")),
                rs.getString("name"));
    }
}
