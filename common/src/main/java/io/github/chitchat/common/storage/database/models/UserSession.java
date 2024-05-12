package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserSession extends BaseModel {
    /** The user this session belongs to. */
    @NonNull private UUID userId;

    /** The token that is used to authenticate the user. */
    @NonNull private String token;

    /** The timestamp when the session is going to expire. */
    @NonNull private Instant expiresAt;

    public UserSession(@NonNull UUID userId, @NonNull String token, @NonNull Instant expiresAt) {
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserSession.class.getSimpleName() + "[", "]")
                .add("userId=" + userId)
                .add("token='" + token + "'")
                .add("expiresAt=" + expiresAt)
                .toString();
    }
}
