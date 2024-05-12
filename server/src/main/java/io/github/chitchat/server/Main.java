package io.github.chitchat.server;

import io.github.chitchat.common.Util;
import io.github.chitchat.common.storage.database.Database;
import io.github.chitchat.server.database.dao.mappers.ServerUserRowMapper;
import io.github.chitchat.server.database.models.ServerUser;
import java.nio.file.Path;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.sqlite.SQLiteDataSource;

public class Main {
    private static final String APP_NAME = "chitchat";
    private static final String DB_NAME = APP_NAME + "_server.db";
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting server...");
        var db = Database.create(createDataSource(), new SQLitePlugin());
        db.registerRowMapper(ServerUser.class, new ServerUserRowMapper());

        new ServerC();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static DataSource createDataSource() {
        var dbPathStr = Util.getEnvOrDefault("DB_PATH", ".");
        var dbPath = Path.of(dbPathStr, DB_NAME);
        dbPath.toFile().getParentFile().mkdirs();

        var datasource = new SQLiteDataSource();
        datasource.setUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());

        return datasource;
    }
}
