package io.github.chitchat.common.storage.database.service.common;

import io.github.chitchat.common.storage.database.dao.common.IBaseDAO;
import io.github.chitchat.common.storage.database.models.common.BaseModel;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import org.jetbrains.annotations.NotNull;

public abstract class BaseService<Dao extends IBaseDAO<Model>, Model extends BaseModel>
        implements IBaseService<Model> {
    protected final Dao dao;

    public BaseService(@NotNull Dao dao) {
        this.dao = dao;
    }

    public int count() {
        return dao.count();
    }

    public abstract void create(@NotNull Model value) throws DuplicateItemException;

    public abstract void update(@NotNull Model value);

    public abstract void delete(@NotNull Model value);
}
