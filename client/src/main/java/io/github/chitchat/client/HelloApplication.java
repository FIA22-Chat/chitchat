package io.github.chitchat.client;

import io.github.chitchat.common.PathUtil;
import io.github.chitchat.common.storage.database.Database;
import java.io.IOException;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdbi.v3.sqlite3.SQLitePlugin;
import org.jetbrains.annotations.NotNull;
import org.sqlite.SQLiteDataSource;

public class HelloApplication extends Application {
    private static final String APP_NAME = "chitchat";
    private static final String DB_NAME = APP_NAME + "_client.db";
    private static final Logger log = LogManager.getLogger(HelloApplication.class);

    public static void main(String[] args) {
        log.info("Starting client...");
        var db = Database.create(createDataSource(), true, new SQLitePlugin());

        launch();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static @NotNull DataSource createDataSource() {
        var dbPathStr =
                PathUtil.getEnvOrDefault("DB_PATH", PathUtil.getUserAppDir(APP_NAME).toString());
        var dbPath = Path.of(dbPathStr, DB_NAME);
        dbPath.toFile().getParentFile().mkdirs();

        var datasource = new SQLiteDataSource();
        datasource.setUrl("jdbc:sqlite:" + dbPath.toAbsolutePath());

        return datasource;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
