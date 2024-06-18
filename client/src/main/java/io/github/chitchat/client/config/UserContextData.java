package io.github.chitchat.client.config;

import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;
import lombok.Data;

@Data
public class UserContextData implements Serializable {
    @Serial private static final long serialVersionUID = -7269728781560704381L;

    private UUID id;
    private String username;
    private EnumSet<PermissionType> permission;
    private Path profilePicture;
    private Locale language;
}
