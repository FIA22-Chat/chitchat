package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;

/** Represents the association between a user and a role. */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserRole extends BaseModel {
    /** The user that has this role */
    @NonNull private UUID userId;

    /** The role that the user has */
    @NonNull private UUID roleId;

    /** The timestamp when a change was made to the user role */
    @NonNull private Instant modifiedAt;

    public UserRole(@NonNull UUID userId, @NonNull UUID roleId, @NonNull Instant modifiedAt) {
        this.userId = userId;
        this.roleId = roleId;
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserRole.class.getSimpleName() + "[", "]")
                .add("userId=" + userId)
                .add("roleId=" + roleId)
                .add("modifiedAt=" + modifiedAt)
                .toString();
    }
}
