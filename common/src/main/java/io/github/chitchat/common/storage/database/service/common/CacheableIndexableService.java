package io.github.chitchat.common.storage.database.service.common;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.github.chitchat.common.storage.database.dao.common.IIndexableDAO;
import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a service that can be used to interact with a database table that has an id column in the
 * format of UUID. This service also caches the data locally.
 *
 * @param <Dao> The DAO that is used to interact with the database
 * @param <Model> The model that is used to represent the data from the database
 */
public abstract class CacheableIndexableService<
                Dao extends IIndexableDAO<UUID, Model>, Model extends IndexableModel>
        extends IndexableService<Dao, Model> {
    protected final LoadingCache<UUID, Model> cache;

    public CacheableIndexableService(@NotNull Dao dao, int cacheSize) {
        super(dao);
        this.cache =
                Caffeine.newBuilder()
                        .maximumSize(cacheSize)
                        .build(k -> dao.getById(k).orElse(null));
    }

    public CacheableIndexableService(
            @NotNull Dao dao, CacheLoader<? super UUID, Model> loader, int cacheSize) {
        super(dao);
        this.cache = Caffeine.newBuilder().maximumSize(cacheSize).build(loader);
    }

    public void clearCache() {
        cache.invalidateAll();
    }

    public void clearCache(UUID id) {
        cache.invalidate(id);
    }

    @Override
    public Optional<Model> get(UUID id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Map<UUID, Model> get(List<UUID> ids) {
        // We take the performance hit of converting the set to a list to avoid multiple calls to
        // the database
        return cache.getAll(
                ids,
                uuids ->
                        dao.getById(new ArrayList<>(uuids)).stream()
                                .collect(Collectors.toMap(Model::getId, m -> m)));
    }

    @Override
    public void create(@NotNull Model value) throws DuplicateItemException {
        if (cache.getIfPresent(value.getId()) != null)
            throw new DuplicateItemException("Item already exists");

        dao.insert(value);
        cache.put(value.getId(), value);
    }

    @Override
    public void update(@NotNull Model value) {
        dao.update(value);
        cache.put(value.getId(), value);
    }

    @Override
    public void delete(@NotNull Model value) {
        dao.delete(value);
        cache.invalidate(value.getId());
    }
}
