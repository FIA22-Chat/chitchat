package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.models.Group;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(Group.class)
public interface GroupDAO<T extends Group> {
    @SqlQuery("select count(*) from \"group\"")
    int count();

    @SqlQuery("select exists(select 1 from \"group\" where id = :id)")
    boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from \"group\" where id = :id)")
    boolean exists(@BindBean T group);

    @SqlQuery("select * from \"group\" order by id")
    List<T> getAll();

    @SqlQuery("select * from \"group\" where id in (<ids>) order by id")
    List<T> getByIds(List<UUID> ids);

    @SqlQuery("select * from \"group\" where id = :id")
    Optional<T> getById(UUID id);

    @SqlQuery("select * from \"group\" where name = :name")
    Optional<T> getByName(String name);

    @Transaction
    @SqlUpdate(
            "insert into \"group\" (id, name, description, modified_at) values"
                    + " (:id, :name, :description, :modifiedAt)")
    void insert(@BindBean T group);

    @Transaction
    @SqlUpdate("delete from \"group\" where id = :id")
    void delete(@BindBean T group);

    @Transaction
    @SqlUpdate(
            "update \"group\" set name = :name, description = :description, modified_at ="
                    + " :modifiedAt where id = :id")
    void update(@BindBean T group);
}
