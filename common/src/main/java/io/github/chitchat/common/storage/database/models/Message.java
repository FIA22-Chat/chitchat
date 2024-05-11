package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/** Represents a message in the database. */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Message extends IndexableModel {
    /** The user that sent the message. */
    @NonNull private UUID userId;

    /** The group that the message was sent to. */
    @NonNull private UUID groupId;

    /** The type of message, see {@link MessageType}. */
    @NonNull private MessageType type;

    /** The content of the message. */
    @NonNull private Byte[] content;

    /** The timestamp when the message was sent. */
    @NonNull private Instant modifiedAt;
}
