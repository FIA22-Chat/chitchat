package io.github.chitchat.server.models;

import io.github.chitchat.common.storage.database.models.User;
import java.time.Instant;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ServerUser extends User {
    @NonNull private String email;

    /** A string representing the Argon2 hash of the password. Contains the salt and the hash. */
    @NonNull private String password;

    /** The timestamp when the user data was last modified. */
    @NonNull private Instant modifiedAt;
}
