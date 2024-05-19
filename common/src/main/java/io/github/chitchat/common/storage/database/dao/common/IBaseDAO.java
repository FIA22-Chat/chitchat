package io.github.chitchat.common.storage.database.dao.common;

public interface IBaseDAO<T> {
    void insert(T t);

    void delete(T t);

    void update(T t);
}
