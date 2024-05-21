package io.github.chitchat.common.storage.database.service.common;

import io.github.chitchat.common.storage.database.dao.common.IIndexableDAO;
import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public abstract class IndexableService<
                Dao extends IIndexableDAO<UUID, Model>, Model extends IndexableModel>
        extends BaseService<Dao, UUID, Model> {

    public IndexableService(@NotNull Dao dao, int cacheSize) {
        super(dao, key -> dao.getById(key).orElse(null), cacheSize);
    }

    public Model get(UUID id) {
        return cache.get(id);
    }

    public Map<UUID, Model> get(Collection<UUID> ids) {
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
            throw new DuplicateItemException("Group already exists");

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
