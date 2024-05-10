package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.util.EnumSet;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Represents a role that can be assigned to a user */
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends IndexableModel {
    /** The group that the role belongs to. */
    private int groupId;

    /** The name of the role. */
    private String name;

    /** The permission scope of the role. */
    private EnumSet<Permission> permission;

    /** The timestamp when the role was modified. */
    private int modifiedAt;
}
