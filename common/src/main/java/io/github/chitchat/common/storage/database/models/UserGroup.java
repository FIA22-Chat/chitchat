package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/** Represent the association between a user and a group. */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class UserGroup extends BaseModel {
    /** The user that is part of the group. */
    @NonNull private UUID userId;

    /** The group that the user is part of. */
    @NonNull private UUID groupId;

    /** The timestamp when a change was made to the user group. */
    @NonNull private Instant modifiedAt;
}
