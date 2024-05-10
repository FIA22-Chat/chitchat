package io.github.chitchat.common;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public class Util {
    private static final String os = System.getProperty("os.name").toLowerCase();

    /**
     * Returns the value of the environment variable with the specified key or the default value if
     * the environment variable is not set ({@code null}).
     *
     * @param key the name of the environment variable
     * @param defaultValue the default value
     * @return the value of the environment variable with the specified key or the default value
     */
    @NotNull public static String getEnvOrDefault(@NotNull String key, @NotNull String defaultValue) {
        var value = System.getenv(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Returns the path to the user's application directory based on the operating system.
     *
     * @param appName the name of the application
     * @return the path to the user's application directory
     */
    @NotNull public static Path getUserAppDir(@NotNull String appName) {
        if (os.contains("win")) {
            return Path.of(System.getenv("LOCALAPPDATA"), appName);
        }
        if (os.contains("mac")) {
            return Path.of(
                    System.getProperty("user.home"), "Library", "Application Support", appName);
        }

        return Path.of(System.getProperty("user.home"), "." + appName);
    }

    /**
     * Returns the path to the user's cache directory based on the operating system.
     *
     * @param appName the name of the application
     * @return the path to the user's cache directory
     */
    @NotNull public static Path getUserCacheDir(@NotNull String appName) {
        var os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return Path.of(System.getProperty("LOCALAPPDATA"), "Temp", appName);
        }
        if (os.contains("mac")) {
            return Path.of(System.getProperty("user.home"), "Library", "Caches", appName);
        }
        if (os.contains("nix")) {
            return Path.of(System.getProperty("user.home"), ".cache", appName);
        }

        return Path.of(System.getProperty("user.home"), "." + appName, "cache");
    }
}
