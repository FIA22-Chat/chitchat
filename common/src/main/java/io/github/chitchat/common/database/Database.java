package io.github.chitchat.common.database;

import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jetbrains.annotations.NotNull;

public class Database {
    /** The name of the database file */
    public static final String DB_NAME = "chitchat.db";

    private static final Logger log = LogManager.getLogger(Database.class);

    /**
     * Creates and configures a new SQLite database at the given path, the database file will be
     * named as {@value DB_NAME}. Required migrations are automatically applied to the database. The
     * caller is responsible for opening and closing the database connection.
     *
     * @param path the path to create the database at
     * @return a new Jdbi instance
     * @throws FlywayException if an error occurs while applying migrations
     */
    public static Jdbi create(@NotNull Path path) throws FlywayException {
        String url = "jdbc:sqlite:" + path.resolve(DB_NAME).toAbsolutePath();
        log.trace("Using database URL: {}", url);

        Flyway.configure()
                .dataSource(url, null, null)
                .locations("classpath:io/github/chitchat/common/migration")
                .load()
                .migrate();
        return Jdbi.create(url)
                .installPlugin(new SQLitePlugin())
                .installPlugin(new SqlObjectPlugin());
    }
}
