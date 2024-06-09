package io.github.chitchat.client.view.components;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public class ChitGroup extends GridPane {
    @FXML private ImageView imageView;
    @FXML private Label name;
    @FXML private Label message;

    public ChitGroup(@NotNull String name, @NotNull String message, @NotNull String imageURL) {
        super();
        load(this);

        this.name.setText(name);
        this.message.setText(message);
        this.imageView.setImage(new Image(imageURL));
    }

    protected static void load(ChitGroup chitGroup) {
        FXMLLoader loader =
                new FXMLLoader(
                        ChitGroup.class.getResource(
                                "/io/github/chitchat/client/components/chitGroup.fxml"));
        loader.setController(chitGroup);
        loader.setRoot(chitGroup);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
