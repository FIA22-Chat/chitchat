package io.github.chitchat.common.storage.database;

import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

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
    public static Jdbi create(DataSource dataSource, JdbiPlugin... jdbiPlugins)
            throws FlywayException {
        log.trace("Using database URL: {}", dataSource.toString());

        Flyway.configure().dataSource(dataSource).load().migrate();
        log.trace("Migrations applied successfully");

        var jdbi = Jdbi.create(dataSource).installPlugin(new SqlObjectPlugin());
        for (JdbiPlugin plugin : jdbiPlugins) {
            log.trace("Installing Jdbi plugin: {}", plugin.getClass().getName());
            jdbi.installPlugin(plugin);
        }

        return jdbi;
    }
}
