package io.github.chitchat.common.storage.database.models;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Represents the association between a user and a role. */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserRole extends BaseModel {
    /** The user that has this role */
    private UUID userId;

    /** The role that the user has */
    private UUID roleId;

    /** The timestamp when a change was made to the user role */
    private int modifiedAt;
}
