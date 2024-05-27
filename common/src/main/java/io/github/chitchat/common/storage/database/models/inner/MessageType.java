package io.github.chitchat.common.storage.database.models.inner;

import java.io.Serializable;

public enum MessageType implements Serializable {
    TEXT,
    MEDIA;

    private static final MessageType[] MESSAGE_TYPES = MessageType.values();

    public static MessageType from(int value) {
        return MESSAGE_TYPES[value];
    }
}
