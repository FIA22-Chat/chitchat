package io.github.chitchat.server.database.models;

import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import java.time.Instant;
import java.util.EnumSet;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;

/** Represents a unique server-side user that can be assigned to a group */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ServerUser extends User {
    /** The email of the user. */
    @NonNull private String email;

    /** A string representing the Argon2 hash of the password. Contains the salt and the hash. */
    @NonNull private String password;

    /** The timestamp when the user data was last modified. */
    @NonNull private Instant modifiedAt;

    public ServerUser(
            @NonNull UUID id,
            @NonNull UserType type,
            @NonNull EnumSet<PermissionType> permission,
            @NonNull String name,
            @NonNull String email,
            @NonNull String password,
            @NonNull Instant modifiedAt) {
        super(id, type, permission, name);
        this.email = email;
        this.password = password;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ServerUser.class.getSimpleName() + "[", "]")
                .add("email='" + email + "'")
                .add("password='" + password + "'")
                .add("modifiedAt=" + modifiedAt)
                .add("type=" + type)
                .add("permission=" + permission)
                .add("name='" + name + "'")
                .add("id=" + id)
                .add("createdAt=" + createdAt)
                .toString();
    }
}
