package io.github.chitchat.common.storage.database.dao.arguments;

import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.types.BitFlag;
import java.sql.Types;
import java.util.EnumSet;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

public class PermissionArgumentFactory extends AbstractArgumentFactory<EnumSet<PermissionType>> {
    public PermissionArgumentFactory() {
        super(Types.BIGINT);
    }

    @Override
    protected Argument build(EnumSet<PermissionType> value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setLong(position, BitFlag.toBitMask(value));
    }
}
