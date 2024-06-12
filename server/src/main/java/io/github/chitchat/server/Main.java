package io.github.chitchat.server;

import io.github.chitchat.common.PathUtil;
import io.github.chitchat.common.filter.ProfanityFilter;
import io.github.chitchat.common.storage.database.Database;
import io.github.chitchat.server.database.dao.mappers.ServerUserRowMapper;
import io.github.chitchat.server.database.models.ServerUser;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteDataSource;

public class Main {
    private static final String APP_NAME = "chitchat";
    private static final String DB_NAME = APP_NAME + "_server.db";
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting server...");
        var db = Database.create(createDataSource(), true, new SQLitePlugin());
        db.registerRowMapper(ServerUser.class, new ServerUserRowMapper());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static @NotNull DataSource createDataSource() {
        var dbPathStr = PathUtil.getEnvOrDefault("DB_PATH", ".");
        var dbPath = Path.of(dbPathStr, DB_NAME);
        dbPath.toFile().getParentFile().mkdirs();

        var datasource = new SQLiteDataSource();
        datasource.setUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());

        return datasource;
    }

    private static @NotNull ProfanityFilter loadProfanityFilter() {
        try (var list = Main.class.getResourceAsStream("list/defaultProfanityList")) {
            var profanities =
                    List.of(new String(Objects.requireNonNull(list).readAllBytes()).split(","));
            return new ProfanityFilter(profanities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load profanity list", e);
        }
    }
}
