package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import java.time.Instant;
import java.util.EnumSet;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;

/** Represents a role that can be assigned to a user */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Role extends IndexableModel {
    /** The group that the role belongs to. */
    @NonNull private UUID groupId;

    /** The name of the role. */
    @NonNull private String name;

    /** The permission scope of the role. */
    @NonNull private EnumSet<PermissionType> permission;

    /** The timestamp when the role was modified. */
    @NonNull private Instant modifiedAt;

    public Role(
            @NonNull UUID id,
            @NonNull UUID groupId,
            @NonNull String name,
            @NonNull EnumSet<PermissionType> permission,
            @NonNull Instant modifiedAt) {
        super(id);
        this.groupId = groupId;
        this.name = name;
        this.permission = permission;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Role.class.getSimpleName() + "[", "]")
                .add("groupId=" + groupId)
                .add("name='" + name + "'")
                .add("permission=" + permission)
                .add("modifiedAt=" + modifiedAt)
                .add("id=" + id)
                .toString();
    }
}
