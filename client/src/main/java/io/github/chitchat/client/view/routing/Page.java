package io.github.chitchat.client.view.routing;

import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ToString
public enum Page {
    LOGIN("login/login.fxml"),
    REGISTER("login/register/register.fxml"),
    MAIN("main/main.fxml"),
    SETTINGS("settings/settings.fxml");

    private static final String BASE_PATH = "pages/";
    private final String path;

    Page(String path) {
        this.path = path;
    }

    @Contract(pure = true)
    public @NotNull String getPath() {
        return BASE_PATH + path;
    }
}
