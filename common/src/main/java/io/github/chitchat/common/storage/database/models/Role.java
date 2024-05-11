package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/** Represents a role that can be assigned to a user */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Role extends IndexableModel {
    /** The group that the role belongs to. */
    @NonNull private UUID groupId;

    /** The name of the role. */
    @NonNull private String name;

    /** The permission scope of the role. */
    @NonNull private EnumSet<Permission> permission;

    /** The timestamp when the role was modified. */
    @NonNull private Instant modifiedAt;
}
