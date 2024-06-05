package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.common.IIndexableDAO;
import io.github.chitchat.common.storage.database.dao.mappers.RoleRowMapper;
import io.github.chitchat.common.storage.database.models.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.GenerateSqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@GenerateSqlObject
@RegisterBeanMapper(Role.class)
public abstract class RoleDAO implements IIndexableDAO<UUID, Role> {
    @SqlQuery("select count(*) from role")
    public abstract int count();

    @SqlQuery("select exists(select 1 from role where id = :id)")
    public abstract boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from role where id = :id)")
    public abstract boolean exists(@BindBean Role role);

    @SqlQuery("select * from role")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getAll();

    @SqlQuery("select * from role where id in (<ids>)")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getById(@BindList("ids") List<UUID> ids);

    @SqlQuery("select * from role where id = :id")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract Optional<Role> getById(UUID id);

    @SqlQuery("select * from role where group_id = :groupId")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getByGroupId(UUID groupId);

    @SqlQuery("select * from role where name = :name")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract Optional<Role> getByName(String name);

    @Transaction
    @SqlUpdate(
            "insert into role (id, group_id, name, permission, modified_at) values"
                    + " (:id, :groupId, :name, :permission, :modifiedAt)")
    public abstract void insert(@BindBean Role role);

    @Transaction
    @SqlUpdate("delete from role where id = :id")
    public abstract void delete(@BindBean Role role);

    @Transaction
    @SqlUpdate(
            "update role set group_id = :groupId, name = :name, permission = :permission,"
                    + " modified_at = :modifiedAt where id = :id")
    public abstract void update(@BindBean Role role);
}
