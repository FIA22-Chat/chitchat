package io.github.chitchat.common.storage.database.service.common;

import io.github.chitchat.common.storage.database.dao.common.IIndexableDAO;
import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a simple service that can be used to interact with a database table that has an id column
 * in the format of UUID.
 *
 * @param <Dao> The DAO that is used to interact with the database
 * @param <Model> The model that is used to represent the data from the database
 */
public abstract class IndexableService<
                Dao extends IIndexableDAO<UUID, Model>, Model extends IndexableModel>
        extends BaseService<Dao, UUID, Model> implements IIndexableService<Model> {

    public IndexableService(@NotNull Dao dao) {
        super(dao);
    }

    @Override
    public Optional<Model> get(UUID id) {
        return dao.getById(id);
    }

    @Override
    public Map<UUID, Model> get(List<UUID> ids) {
        return dao.getById(ids).stream().collect(Collectors.toMap(Model::getId, m -> m));
    }

    @Override
    public void create(@NotNull Model value) throws DuplicateItemException {
        dao.insert(value);
    }

    @Override
    public void update(@NotNull Model value) {
        dao.update(value);
    }

    @Override
    public void delete(@NotNull Model value) {
        dao.delete(value);
    }
}
