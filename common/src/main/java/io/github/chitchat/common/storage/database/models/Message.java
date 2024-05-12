package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import io.github.chitchat.common.storage.database.models.inner.MessageType;
import java.time.Instant;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;

/** Represents a message in the database. */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Message extends IndexableModel {
    /** The user that sent the message. */
    @NonNull private UUID userId;

    /** The group that the message was sent to. */
    @NonNull private UUID groupId;

    /** The type of message, see {@link MessageType}. */
    @NonNull private MessageType type;

    /** The content of the message. */
    private byte @NonNull [] content;

    /** The timestamp when the message was sent. */
    @NonNull private Instant modifiedAt;

    public Message(
            @NonNull UUID id,
            @NonNull UUID userId,
            @NonNull UUID groupId,
            @NonNull MessageType type,
            byte @NonNull [] content,
            @NonNull Instant modifiedAt) {
        super(id);
        this.userId = userId;
        this.groupId = groupId;
        this.type = type;
        this.content = content;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Message.class.getSimpleName() + "[", "]")
                .add("userId=" + userId)
                .add("groupId=" + groupId)
                .add("type=" + type)
                .add("content=" + Arrays.toString(content))
                .add("modifiedAt=" + modifiedAt)
                .add("id=" + id)
                .toString();
    }
}
