package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/** Represents the association between a user and a role. */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class UserRole extends BaseModel {
    /** The user that has this role */
    @NonNull private UUID userId;

    /** The role that the user has */
    @NonNull private UUID roleId;

    /** The timestamp when a change was made to the user role */
    @NonNull private Instant modifiedAt;
}
