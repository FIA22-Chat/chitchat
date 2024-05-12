package io.github.chitchat.common.storage.database.models.inner;

public enum MessageType {
    TEXT,
    MEDIA;

    private static final MessageType[] MESSAGE_TYPES = MessageType.values();

    public static MessageType from(int value) {
        return MESSAGE_TYPES[value];
    }
}
