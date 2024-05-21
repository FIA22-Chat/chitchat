package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.time.Instant;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.*;

/** Represent the association between a user and a group. */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserGroup extends BaseModel {
    /** The user that is part of the group. */
    @NonNull private UUID userId;

    /** The group that the user is part of. */
    @NonNull private UUID groupId;

    /** The timestamp when a change was made to the user group. */
    @NonNull private Instant modifiedAt;

    public UserGroup(@NonNull UUID userId, @NonNull UUID groupId, @NonNull Instant modifiedAt) {
        this.userId = userId;
        this.groupId = groupId;
        this.modifiedAt = modifiedAt;
    }

    public UserGroup(@NonNull User user, @NonNull Group group, @NonNull Instant modifiedAt) {
        this.userId = user.getId();
        this.groupId = group.getId();
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserGroup.class.getSimpleName() + "[", "]")
                .add("userId=" + userId)
                .add("groupId=" + groupId)
                .add("modifiedAt=" + modifiedAt)
                .toString();
    }
}
