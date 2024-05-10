package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Represent the association between a user and a group. */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserGroup extends BaseModel {
    /** The user that is part of the group. */
    private UUID userId;

    /** The group that the user is part of. */
    private UUID groupId;

    /** The timestamp when a change was made to the user group. */
    private int modifiedAt;
}
