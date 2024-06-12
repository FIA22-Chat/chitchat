package io.github.chitchat.common.storage.database.service.common;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import org.jetbrains.annotations.NotNull;

public interface IBaseService<Model extends BaseModel> {
    int count();

    void create(@NotNull Model value) throws DuplicateItemException;

    void update(@NotNull Model value);

    void delete(@NotNull Model value);
}
