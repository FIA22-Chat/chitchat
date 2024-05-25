package io.github.chitchat.client.config;

import java.io.Serial;
import java.io.Serializable;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.Data;

@Data
public class UserSettings implements Serializable {
    @Serial private static final long serialVersionUID = -8954655507956526824L;
    private transient Rectangle2D screenBounds = Screen.getPrimary().getBounds();

    private double sceneWidth = Math.min(screenBounds.getMaxX(), 1400);
    private double sceneHeight = Math.min(screenBounds.getMaxY(), 800);
    private double stageX = screenBounds.getMaxX() / 2 - sceneWidth / 2;
    private double stageY = screenBounds.getMaxY() / 2 - sceneHeight / 2;
    private boolean isMaximized = false;
    private boolean alwaysOnTop = false;
    private boolean isFullscreen = false;
}
