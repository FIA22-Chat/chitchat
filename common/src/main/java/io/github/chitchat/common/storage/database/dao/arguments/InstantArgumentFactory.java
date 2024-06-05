package io.github.chitchat.common.storage.database.dao.arguments;

import java.sql.Types;
import java.time.Instant;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

public class InstantArgumentFactory extends AbstractArgumentFactory<Instant> {
    public InstantArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(Instant value, ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.toString());
    }
}
