package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.models.Role;
import java.util.List;
import java.util.Optional;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(Role.class)
public interface RoleDAO<T extends Role> {
    @SqlQuery("select count(*) from role")
    int count();

    @SqlQuery("select exists(select 1 from role where id = :id)")
    boolean existsById(int id);

    @SqlQuery("select exists(select 1 from role where id = :id)")
    boolean exists(T role);

    @SqlQuery("select * from role order by id")
    List<T> getAll();

    @SqlQuery("select * from role where id in (<ids>) order by id")
    List<T> getByIds(List<Integer> ids);

    @SqlQuery("select * from role where id = :id")
    Optional<T> getById(int id);

    @SqlQuery("select * from role where group_id = :groupId")
    List<T> getByGroupId(int groupId);

    @SqlQuery("select * from role where name = :name")
    Optional<T> getByName(String name);

    @Transaction
    @SqlUpdate(
            "insert into role (id, group_id, name, permission, modified_at) values"
                    + " (:id, :groupId, :name, :permission, :modifiedAt)")
    void insert(T role);

    @Transaction
    @SqlUpdate("delete from role where id = :id")
    void delete(T role);

    @Transaction
    @SqlUpdate(
            "update role set group_id = :groupId, name = :name, permission = :permission,"
                    + " modified_at = :modifiedAt where id = :id")
    void update(T role);
}
