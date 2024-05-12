package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import java.util.EnumSet;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;
import org.jetbrains.annotations.NotNull;

/** Represents a unique user that can be assigned to a group */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends IndexableModel {
    /** The type of user, see {@link UserType}. */
    @NonNull protected UserType type;

    /**
     * A set of global level permissions that the user has assigned to them, these always take
     * precedence over group level permissions.
     */
    @NonNull protected EnumSet<PermissionType> permission;

    /** The name of the user. */
    @NonNull protected String name;

    public User(
            @NotNull UUID id,
            @NotNull UserType type,
            @NotNull EnumSet<PermissionType> permission,
            @NotNull String name) {
        super(id);
        this.type = type;
        this.permission = permission;
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("type=" + type)
                .add("permission=" + permission)
                .add("name='" + name + "'")
                .toString();
    }
}
