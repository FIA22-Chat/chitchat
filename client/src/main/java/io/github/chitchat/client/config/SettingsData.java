package io.github.chitchat.client.config;

import java.io.Serial;
import java.io.Serializable;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.Data;

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
}
