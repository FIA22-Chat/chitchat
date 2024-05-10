package io.github.chitchat.common.storage.database.models;

import java.io.Serializable;

public enum Permission implements Serializable {
    SEND_MESSAGE,
    DELETE_MESSAGE,
    EDIT_MESSAGE
}
