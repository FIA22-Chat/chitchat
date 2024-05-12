package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.mappers.RoleRowMapper;
import io.github.chitchat.common.storage.database.models.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(Role.class)
public interface RoleDAO {
    @SqlQuery("select count(*) from role")
    int count();

    @SqlQuery("select exists(select 1 from role where id = :id)")
    boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from role where id = :id)")
    boolean exists(@BindBean Role role);

    @SqlQuery("select * from role order by id")
    @RegisterRowMapper(RoleRowMapper.class)
    List<Role> getAll();

    @SqlQuery("select * from role where id in (<ids>) order by id")
    @RegisterRowMapper(RoleRowMapper.class)
    List<Role> getByIds(@BindList("ids") List<UUID> ids);

    @SqlQuery("select * from role where id = :id")
    @RegisterRowMapper(RoleRowMapper.class)
    Optional<Role> getById(UUID id);

    @SqlQuery("select * from role where group_id = :groupId")
    @RegisterRowMapper(RoleRowMapper.class)
    List<Role> getByGroupId(UUID groupId);

    @SqlQuery("select * from role where name = :name")
    @RegisterRowMapper(RoleRowMapper.class)
    Optional<Role> getByName(String name);

    @Transaction
    @SqlUpdate(
            "insert into role (id, group_id, name, permission, modified_at) values"
                    + " (:id, :groupId, :name, :permission, :modifiedAt)")
    void insert(@BindBean Role role);

    @Transaction
    @SqlUpdate("delete from role where id = :id")
    void delete(@BindBean Role role);

    @Transaction
    @SqlUpdate(
            "update role set group_id = :groupId, name = :name, permission = :permission,"
                    + " modified_at = :modifiedAt where id = :id")
    void update(@BindBean Role role);
}
