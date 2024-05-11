package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class UserSession extends BaseModel {
    /** The user this session belongs to. */
    @NonNull private UUID userId;

    /** The token that is used to authenticate the user. */
    @NonNull private String token;

    /** The timestamp when the session is going to expire. */
    @NonNull private Instant expiresAt;
}
