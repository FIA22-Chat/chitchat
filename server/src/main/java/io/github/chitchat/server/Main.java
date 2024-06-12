package io.github.chitchat.server;

import io.github.chitchat.common.CommonUtil;
import io.github.chitchat.common.filter.ProfanityFilter;
import io.github.chitchat.common.storage.database.Database;
import io.github.chitchat.server.database.dao.mappers.ServerUserRowMapper;
import io.github.chitchat.server.database.dao.mappers.ServerUserSessionRowMapper;
import io.github.chitchat.server.database.models.ServerUser;
import io.github.chitchat.server.database.models.ServerUserSession;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
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
        db.registerRowMapper(ServerUserSession.class, new ServerUserSessionRowMapper());

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

    public static @NotNull ProfanityFilter loadProfanityFilter() {
        try (var list = Main.class.getResourceAsStream("list/defaultProfanityList")) {
            var profanities =
                    List.of(new String(Objects.requireNonNull(list).readAllBytes()).split(","));
            return new ProfanityFilter(profanities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load profanity list", e);
        }
    }
}
