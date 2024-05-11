package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.models.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(User.class)
public interface UserDAO<T extends User> {
    @SqlQuery("select count(*) from user")
    int count();

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean exists(@BindBean T user);

    @SqlQuery("select * from user order by id")
    List<T> getAll();

    @SqlQuery("select * from user where id in (<ids>) order by id")
    List<T> getByIds(List<UUID> ids);

    @SqlQuery("select * from user where id = :id")
    Optional<T> getById(UUID id);

    @SqlQuery("select * from user where name = :name")
    Optional<T> getByName(String name);

    @Transaction
    @SqlUpdate(
            "insert into user (id, type, permission, name) values (:id, :type, :permission, :name)")
    void insert(@BindBean T user);

    @Transaction
    @SqlUpdate("delete from user where id = :id")
    void delete(@BindBean T user);

    @Transaction
    @SqlUpdate(
            "update user set type = :type, permission = :permission, name = :name where id = :id")
    void update(@BindBean T user);
}
