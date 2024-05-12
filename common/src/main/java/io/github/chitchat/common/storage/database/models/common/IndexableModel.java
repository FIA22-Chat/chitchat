package io.github.chitchat.common.storage.database.models.common;

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
public class IndexableModel extends BaseModel {
    /**
     * A unique identifier in the format of UUID v7 which includes a timestamp. This enables the
     * model to be sorted chronologically.
     */
    @NonNull @EqualsAndHashCode.Include protected UUID id;
}
