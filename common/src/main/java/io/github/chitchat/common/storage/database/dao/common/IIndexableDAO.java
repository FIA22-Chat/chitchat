package io.github.chitchat.common.storage.database.dao.common;

import java.util.List;
import java.util.Optional;

public interface IIndexableDAO<K, V> extends IBaseDAO<V> {
    boolean existsById(K id);

    Optional<V> getById(K id);

    List<V> getById(List<K> ids);
}
