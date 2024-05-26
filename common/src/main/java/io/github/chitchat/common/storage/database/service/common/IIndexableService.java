package io.github.chitchat.common.storage.database.service.common;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import java.util.*;

public interface IIndexableService<Model extends IndexableModel> extends IBaseService<Model> {
    Optional<Model> get(UUID id);

    Map<UUID, Model> get(List<UUID> ids);
}
