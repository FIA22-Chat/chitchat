package io.github.chitchat.server.models;

import io.github.chitchat.common.storage.database.models.User;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServerUser extends User {
    private String email;

    /** A string representing the Argon2 hash of the password. Contains the salt and the hash. */
    private String password;

    /** The timestamp when the user data was last modified. */
    private Instant modifiedAt;
}
