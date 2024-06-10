package io.github.chitchat.server;

import io.github.chitchat.common.CommonUtil;
import io.github.chitchat.common.storage.database.Database;
import io.github.chitchat.server.database.dao.mappers.ServerUserRowMapper;
import io.github.chitchat.server.database.models.ServerUser;
import java.nio.file.Path;
import javax.sql.DataSource;
import lombok.extern.log4j.Log4j2;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteDataSource;

@Log4j2
public class Main {
    private static final String APP_NAME = "ChitChat";
    private static final String DB_NAME = APP_NAME + "_server.db";

    public static void main(String[] args) {
        log.info("Initializing {}...", APP_NAME);

        var db = Database.create(createDataSource(), true, new SQLitePlugin());
        db.registerRowMapper(ServerUser.class, new ServerUserRowMapper());

        var port = Integer.parseInt(CommonUtil.getEnvOrDefault("PORT", "8080"));

        var app = new App(port, db);
        var appThread = new Thread(app);

        // Graceful shutdown hook
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    log.info("Shutting down {}...", APP_NAME);
                                    app.stop();
                                }));

        appThread.start();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static @NotNull DataSource createDataSource() {
        var dbPathStr = CommonUtil.getEnvOrDefault("DB_PATH", ".");
        var dbPath = Path.of(dbPathStr, DB_NAME);
        dbPath.toFile().getParentFile().mkdirs();

        var datasource = new SQLiteDataSource();
        datasource.setUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());

        return datasource;
    }
}
