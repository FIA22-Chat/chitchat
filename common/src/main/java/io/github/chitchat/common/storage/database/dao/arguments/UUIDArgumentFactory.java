package io.github.chitchat.common.storage.database.dao.arguments;

import com.fasterxml.uuid.impl.UUIDUtil;
import java.sql.Types;
import java.util.UUID;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

public class UUIDArgumentFactory extends AbstractArgumentFactory<UUID> {
    public UUIDArgumentFactory() {
        super(Types.BLOB);
    }

    @Override
    protected Argument build(UUID value, ConfigRegistry config) {
        return (position, statement, ctx) ->
                statement.setBytes(position, UUIDUtil.asByteArray(value));
    }
}
