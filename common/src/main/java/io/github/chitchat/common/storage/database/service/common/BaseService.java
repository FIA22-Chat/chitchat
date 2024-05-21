package io.github.chitchat.common.storage.database.service.common;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.chitchat.common.storage.database.dao.common.IBaseDAO;
import io.github.chitchat.common.storage.database.models.common.BaseModel;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import org.jetbrains.annotations.NotNull;

public abstract class BaseService<Dao extends IBaseDAO<Model>, Key, Model extends BaseModel>
        implements IBaseService<Model> {
    protected final Dao dao;
    protected final LoadingCache<Key, Model> cache;

    public BaseService(@NotNull Dao dao, CacheLoader<? super Key, Model> loader, int cacheSize) {
        this.dao = dao;
        this.cache = Caffeine.newBuilder().maximumSize(cacheSize).build(loader);
    }

    public abstract void create(@NotNull Model value) throws DuplicateItemException;

    public abstract void update(@NotNull Model value);

    public abstract void delete(@NotNull Model value);
}
