package io.github.chitchat.common.database.models;

import java.time.Instant;
import java.util.EnumSet;
import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    /** A unique identifier in the format of UUID v7 which includes a timestamp. */
    @EqualsAndHashCode.Include private UUID id;

    /** The type of user, see {@link UserType}. */
    private UserType type;

    /**
     * A set of global level permissions that the user has assigned to them, these always take
     * precedence over group level permissions.
     */
    private EnumSet<UserPermission> permission;

    private String name;

    private String email;

    /** A string representing the Argon2 hash of the password. Contains the salt and the hash. */
    private String password;

    /** The timestamp when the user data was last modified. */
    private Instant modifiedAt;
}
