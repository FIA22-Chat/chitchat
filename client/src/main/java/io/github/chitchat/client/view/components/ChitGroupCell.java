package io.github.chitchat.client.view.components;

import javafx.scene.control.ListCell;

public class ChitGroupCell extends ListCell<ChitGroup> {

    public ChitGroupCell() {
        super();
    }

    @Override
    protected void updateItem(ChitGroup item, boolean empty) {
        super.updateItem(item, empty);

        setGraphic(item);
        setText(null);
    }
}
