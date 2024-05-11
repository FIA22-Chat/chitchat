package io.github.chitchat.common.storage.database.arguments;

import io.github.chitchat.common.storage.database.models.Permission;
import java.sql.Types;
import java.util.EnumSet;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

public class PermissionArgumentFactory extends AbstractArgumentFactory<EnumSet<Permission>> {
    public PermissionArgumentFactory() {
        super(Types.INTEGER);
    }

    @Override
    protected Argument build(EnumSet<Permission> value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setInt(position, 0); // todo
    }
}
