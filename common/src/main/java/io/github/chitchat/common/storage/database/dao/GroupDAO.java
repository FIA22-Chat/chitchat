package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.common.IIndexableDAO;
import io.github.chitchat.common.storage.database.dao.mappers.GroupRowMapper;
import io.github.chitchat.common.storage.database.models.Group;
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
@RegisterBeanMapper(Group.class)
public abstract class GroupDAO implements IIndexableDAO<UUID, Group> {
    @SqlQuery("select count(*) from \"group\"")
    public abstract int count();

    @SqlQuery("select exists(select 1 from \"group\" where id = :id)")
    public abstract boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from \"group\" where id = :id)")
    public abstract boolean exists(@BindBean Group group);

    @SqlQuery("select * from \"group\" order by id")
    @RegisterRowMapper(GroupRowMapper.class)
    public abstract List<Group> getAll();

    @SqlQuery("select * from \"group\" where id in (<ids>) order by id")
    @RegisterRowMapper(GroupRowMapper.class)
    public abstract List<Group> getById(@BindList("ids") List<UUID> ids);

    @SqlQuery("select * from \"group\" where id = :id")
    @RegisterRowMapper(GroupRowMapper.class)
    public abstract Optional<Group> getById(UUID id);

    @SqlQuery("select * from \"group\" where name = :name")
    @RegisterRowMapper(GroupRowMapper.class)
    public abstract Optional<Group> getByName(String name);

    @Transaction
    @SqlUpdate(
            "insert into \"group\" (id, name, description, modified_at) values"
                    + " (:id, :name, :description, :modifiedAt)")
    public abstract void insert(@BindBean Group group);

    @Transaction
    @SqlUpdate("delete from \"group\" where id = :id")
    public abstract void delete(@BindBean Group group);

    @Transaction
    @SqlUpdate(
            "update \"group\" set name = :name, description = :description, modified_at ="
                    + " :modifiedAt where id = :id")
    public abstract void update(@BindBean Group group);
}
