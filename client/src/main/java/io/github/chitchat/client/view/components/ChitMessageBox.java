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

    @FXML private VBox wrapper;
    @FXML private GridPane grid;

    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Label messageContent;

    public ChitMessageBox(
            String username, String content, @Nullable String userImagePath, Pos pos) {
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

        if (pos == Pos.CENTER_RIGHT) {
            positionRight.set(true);
            positionLeft.set(false);
        } else {
            positionLeft.set(true);
            positionRight.set(false);
        }

        wrapper.setAlignment(pos);
        this.userName.setText(username);
        this.messageContent.setText(content);
        if (userImagePath != null) this.userImage.setImage(new Image(userImagePath));
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
