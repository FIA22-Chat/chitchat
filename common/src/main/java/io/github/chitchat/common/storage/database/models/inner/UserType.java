package io.github.chitchat.common.storage.database.models.inner;

import java.io.Serializable;

public enum UserType implements Serializable {
    USER,
    BOT;

    private static final UserType[] USER_TYPES = UserType.values();

    public static UserType from(int value) {
        return USER_TYPES[value];
    }
}
