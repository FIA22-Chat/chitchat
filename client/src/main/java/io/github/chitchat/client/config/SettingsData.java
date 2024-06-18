package io.github.chitchat.client.config;

import java.io.Serial;
import java.io.Serializable;
import java.util.Locale;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class SettingsData implements Serializable {
    @Serial private static final long serialVersionUID = -8954655507956526824L;
    private static final Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    private static final double DEFAULT_SCENE_WIDTH = 1400;
    private static final double DEFAULT_SCENE_HEIGHT = 800;

    private double sceneWidth = Math.min(screenBounds.getMaxX(), DEFAULT_SCENE_WIDTH);
    private double sceneHeight = Math.min(screenBounds.getMaxY(), DEFAULT_SCENE_HEIGHT);
    private double stageX = screenBounds.getMaxX() / 2 - sceneWidth / 2;
    private double stageY = screenBounds.getMaxY() / 2 - sceneHeight / 2;
    private boolean isMaximized;
    private boolean isAlwaysOnTop;
    private boolean isFullscreen;

    private Locale locale = Locale.getDefault();

    public void applyStageSettings(@NotNull Stage stage) {
        stage.setX(this.getStageX());
        stage.setY(this.getStageY());
        stage.setWidth(this.getSceneWidth());
        stage.setHeight(this.getSceneHeight());
        stage.setMaximized(this.isMaximized());
        stage.setAlwaysOnTop(this.isAlwaysOnTop());
        stage.setFullScreen(this.isFullscreen());
    }

    public void storeStageSettings(@NotNull Stage stage) {
        this.setStageX(stage.getX());
        this.setStageY(stage.getY());
        this.setSceneWidth(stage.getWidth());
        this.setSceneHeight(stage.getHeight());
        this.setMaximized(stage.isMaximized());
        this.setAlwaysOnTop(stage.isAlwaysOnTop());
        this.setFullscreen(stage.isFullScreen());
    }


}
