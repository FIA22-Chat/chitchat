package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.util.EnumSet;
import lombok.*;

/** Represents a unique user that can be assigned to a group */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends IndexableModel {
    /** The type of user, see {@link UserType}. */
    private UserType type;

    /**
     * A set of global level permissions that the user has assigned to them, these always take
     * precedence over group level permissions.
     */
    private EnumSet<Permission> permission;

    /** The name of the user. */
    private String name;
}
