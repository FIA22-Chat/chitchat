package io.github.chitchat.client.view.components;

import java.io.IOException;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class ChitMessageBox extends VBox {
    private static final PseudoClass POS_RIGHT = PseudoClass.getPseudoClass("right-pos");
    private static final PseudoClass POS_LEFT = PseudoClass.getPseudoClass("left-pos");

    @Getter private final BooleanPropertyBase positionRight;
    @Getter private final BooleanPropertyBase positionLeft;

    @FXML private GridPane grid;
    @FXML private VBox messageImageWrapper;
    @FXML private VBox messageContentWrapper;

    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label messageContent;

    public ChitMessageBox(String userName, String content, Pos pos) {
        this(userName, content, null, pos);
    }

    public ChitMessageBox(String content, Pos pos) {
        this(null, content, null, pos);
    }

    public ChitMessageBox(
            @Nullable String userName, String content, @Nullable String userImagePath, Pos pos) {
        super();
        getStyleClass().addAll("chit-message-box");
        load(this);

        positionRight =
                new BooleanPropertyBase() {
                    @Override
                    protected void invalidated() {
                        pseudoClassStateChanged(POS_RIGHT, get());
                    }

                    @Override
                    public Object getBean() {
                        return ChitMessageBox.this;
                    }

                    @Override
                    public String getName() {
                        return "right-pos";
                    }
                };
        positionLeft =
                new BooleanPropertyBase() {
                    @Override
                    protected void invalidated() {
                        pseudoClassStateChanged(POS_LEFT, get());
                    }

                    @Override
                    public Object getBean() {
                        return ChitMessageBox.this;
                    }

                    @Override
                    public String getName() {
                        return "left-pos";
                    }
                };

        if (pos == Pos.CENTER_RIGHT || pos == Pos.BOTTOM_RIGHT || pos == Pos.TOP_RIGHT) {
            positionRight.set(true);
            positionLeft.set(false);

            grid.setAlignment(Pos.CENTER_RIGHT);
            GridPane.setColumnIndex(messageImageWrapper, 1);
            GridPane.setColumnIndex(messageContentWrapper, 0);
        } else {
            positionLeft.set(true);
            positionRight.set(false);

            grid.setAlignment(Pos.CENTER_LEFT);
            GridPane.setColumnIndex(messageImageWrapper, 0);
            GridPane.setColumnIndex(messageContentWrapper, 1);
        }

        // Remove userName if not provided
        if (userName == null) {
            messageContentWrapper.getChildren().remove(this.userName);
        } else {
            this.userName.setText(userName);
        }

        // Remove userImage if not provided
        if (userImagePath == null) {
            this.userImage.setVisible(false);
        } else {
            this.userImage.setImage(new Image(userImagePath));
        }

        this.messageContent.setText(content);
    }

    protected static void load(ChitMessageBox chitGroup) {
        FXMLLoader loader =
                new FXMLLoader(
                        ChitGroup.class.getResource(
                                "/io/github/chitchat/client/components/chitMessageBox.fxml"));
        loader.setController(chitGroup);
        loader.setRoot(chitGroup);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
