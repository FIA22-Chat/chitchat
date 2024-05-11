package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/** Represents a group in the database. */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Group extends IndexableModel {
    /** The name of the group. */
    @NonNull private String name;

    /** Can be used to describe the purpose of the group or contain any other information */
    @NonNull private String description;

    /** The timestamp when the group was modified. */
    @NonNull private Instant modifiedAt;
}
