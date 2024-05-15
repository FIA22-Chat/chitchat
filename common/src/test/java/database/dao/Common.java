package database.dao;

import io.github.chitchat.common.storage.database.Database;

import java.io.File;
import java.nio.file.Path;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteDataSource;

public class Common {
    static final Path PATH = Path.of("src/test/resources").toAbsolutePath();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static @NotNull Jdbi createDB(String name) {
        var dbPath = PATH.resolve(name);
        File file = dbPath.toFile();
        file.delete(); // Ensure the file is deleted
        file.deleteOnExit(); // Ensure the file is deleted on exit
        file.getParentFile().mkdirs();

        var datasource = new SQLiteDataSource();
        datasource.setUrl("jdbc:sqlite:" + dbPath);

        return Database.create(datasource, false, new SQLitePlugin());
    }
}
