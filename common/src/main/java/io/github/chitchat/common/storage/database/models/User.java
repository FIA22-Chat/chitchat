package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.util.EnumSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** Represents a unique user that can be assigned to a group */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class User extends IndexableModel {
    /** The type of user, see {@link UserType}. */
    @NonNull private UserType type;

    /**
     * A set of global level permissions that the user has assigned to them, these always take
     * precedence over group level permissions.
     */
    @NonNull private EnumSet<Permission> permission;

    /** The name of the user. */
    @NonNull private String name;
}
