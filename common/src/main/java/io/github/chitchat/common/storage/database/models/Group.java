package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;

/** Represents a group in the database. */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Group extends IndexableModel {
    /** The name of the group. */
    @NonNull private String name;

    /** Can be used to describe the purpose of the group or contain any other information */
    @NonNull private String description;

    /** The timestamp when the group was modified. */
    @NonNull private Instant modifiedAt;

    public Group(
            @NonNull UUID id,
            @NonNull String name,
            @NonNull String description,
            @NonNull Instant modifiedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Group.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("description='" + description + "'")
                .add("modifiedAt=" + modifiedAt)
                .add("id=" + id)
                .add("createdAt=" + createdAt)
                .toString();
    }
}
