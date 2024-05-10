package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Represents a message in the database. */
@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends IndexableModel {
    /** The user that sent the message. */
    private UUID userId;

    /** The group that the message was sent to. */
    private UUID groupId;

    /** The type of message, see {@link MessageType}. */
    private MessageType type;

    /** The content of the message. */
    private Byte[] content;

    /** The timestamp when the message was sent. */
    private Instant modifiedAt;
}
