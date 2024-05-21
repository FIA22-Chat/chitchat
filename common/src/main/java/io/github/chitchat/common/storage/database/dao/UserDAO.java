package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.common.IIndexableDAO;
import io.github.chitchat.common.storage.database.dao.mappers.UserRowMapper;
import io.github.chitchat.common.storage.database.models.User;
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
@RegisterBeanMapper(User.class)
public abstract class UserDAO implements IIndexableDAO<UUID, User> {
    @SqlQuery("select count(*) from user")
    public abstract int count();

    @SqlQuery("select exists(select 1 from user where id = :id)")
    public abstract boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from user where id = :id)")
    public abstract boolean exists(@BindBean User user);

    @SqlQuery("select * from user")
    @RegisterRowMapper(UserRowMapper.class)
    public abstract List<User> getAll();

    @SqlQuery("select * from user where id in (<ids>)")
    @RegisterRowMapper(UserRowMapper.class)
    public abstract List<User> getById(@BindList("ids") List<UUID> ids);

    @SqlQuery("select * from user where id = :id")
    @RegisterRowMapper(UserRowMapper.class)
    public abstract Optional<User> getById(UUID id);

    @SqlQuery("select * from user where name = :name")
    @RegisterRowMapper(UserRowMapper.class)
    public abstract Optional<User> getByName(String name);

    @Transaction
    @SqlUpdate(
            "insert into user (id, type, permission, name) values (:id, :type, :permission, :name)")
    public abstract void insert(@BindBean User user);

    @Transaction
    @SqlUpdate("delete from user where id = :id")
    public abstract void delete(@BindBean User user);

    @Transaction
    @SqlUpdate(
            "update user set type = :type, permission = :permission, name = :name where id = :id")
    public abstract void update(@BindBean User user);
}
