package io.github.chitchat.common.storage.database;

import io.github.chitchat.common.storage.database.dao.arguments.InstantArgumentFactory;
import io.github.chitchat.common.storage.database.dao.arguments.PermissionArgumentFactory;
import io.github.chitchat.common.storage.database.dao.arguments.UUIDArgumentFactory;
import io.github.chitchat.common.storage.database.dao.mappers.*;
import io.github.chitchat.common.storage.database.models.*;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.enums.EnumStrategy;
import org.jdbi.v3.core.enums.Enums;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jetbrains.annotations.NotNull;

public class Database {
    private static final Logger log = LogManager.getLogger(Database.class);

    /**
     * Creates and configures a new Jdbi instance with the provided data source.
     *
     * <p>Migrations will be automatically applied to the database and are required to be located
     * under the {@code db/migration} directory in the classpath.
     *
     * <p>The caller is responsible for opening and closing the database connection.
     *
     * @param dataSource the data source to connect to
     * @param jdbiPlugins the Jdbi plugins to install, includes the SqlObjectPlugin by default
     * @return a new Jdbi instance
     * @throws FlywayException if an error occurs while applying migrations
     */
    public static @NotNull Jdbi create(
            @NotNull DataSource dataSource,
            boolean runMigration,
            JdbiPlugin @NotNull ... jdbiPlugins)
            throws FlywayException {
        log.trace("Using database URL: {}", dataSource.toString());

        if (runMigration) {
            log.trace("Applying migrations");
            Flyway.configure().dataSource(dataSource).load().migrate();
            log.trace("Migrations applied successfully");
        } else {
            log.trace("Skipping migrations");
        }

        log.trace("Registering argument factories");
        var jdbi =
                Jdbi.create(dataSource)
                        .registerArgument(new UUIDArgumentFactory())
                        .registerArgument(new PermissionArgumentFactory())
                        .registerArgument(new InstantArgumentFactory())
                        .registerRowMapper(User.class, new UserRowMapper())
                        .registerRowMapper(Role.class, new RoleRowMapper())
                        .registerRowMapper(Group.class, new GroupRowMapper())
                        .registerRowMapper(Message.class, new MessageRowMapper())
                        .registerRowMapper(UserGroup.class, new UserGroupRowMapper())
                        .registerRowMapper(UserRole.class, new UserRoleRowMapper());
        jdbi.getConfig(Enums.class).setEnumStrategy(EnumStrategy.BY_ORDINAL);

        log.trace("Installing Jdbi plugin: {}", SqlObjectPlugin.class.getName());
        jdbi.installPlugin(new SqlObjectPlugin());
        for (JdbiPlugin plugin : jdbiPlugins) {
            log.trace("Installing Jdbi plugin: {}", plugin.getClass().getName());
            jdbi.installPlugin(plugin);
        }

        return jdbi;
    }
}
