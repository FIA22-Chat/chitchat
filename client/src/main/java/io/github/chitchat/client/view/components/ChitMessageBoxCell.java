package io.github.chitchat.client.view.components;

import javafx.scene.control.ListCell;

public class ChitMessageBoxCell extends ListCell<ChitMessageBox> {

    public ChitMessageBoxCell() {
        super();
    }

    @Override
    protected void updateItem(ChitMessageBox item, boolean empty) {
        super.updateItem(item, empty);

        setGraphic(item);
        setText(null);
    }
}
