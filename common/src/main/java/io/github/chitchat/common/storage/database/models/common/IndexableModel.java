package io.github.chitchat.common.storage.database.models.common;

import com.fasterxml.uuid.impl.UUIDUtil;
import java.time.Instant;
import java.util.UUID;
import lombok.*;

/**
 * The {@link IndexableModel} class is the base class for all models that have an index. All models
 * that extend this are considered indexable (read: have an index).
 *
 * <p>This class always inherits the serializable interface from {@link BaseModel}.
 *
 * @apiNote When using lombok annotations, the {@code @EqualsAndHashCode} annotation should be set
 *     to {@code callSuper = true}
 * @see BaseModel
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public abstract class IndexableModel extends BaseModel {
    /**
     * A unique identifier in the format of UUID v7 which includes a timestamp. This enables the
     * model to be sorted chronologically.
     */
    @NonNull @EqualsAndHashCode.Include protected UUID id;

    /**
     * The timestamp when the model was created. This is extracted from the {@link #id} field.
     *
     * @apiNote This field is immutable and cannot be changed.
     */
    @NonNull @Setter(AccessLevel.NONE)
    protected Instant createdAt;

    public IndexableModel(@NonNull UUID id) {
        this.id = id;
        this.createdAt = Instant.ofEpochMilli(UUIDUtil.extractTimestamp(id));
    }
}
