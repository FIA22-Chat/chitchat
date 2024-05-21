package io.github.chitchat.server.database.dao;

import io.github.chitchat.server.database.dao.mappers.ServerUserRowMapper;
import io.github.chitchat.server.database.models.ServerUser;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(ServerUser.class)
public interface ServerUserDAO {
    @SqlQuery("select count(*) from user")
    int count();

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean exists(@BindBean ServerUser user);

    @SqlQuery("select * from user")
    @RegisterRowMapper(ServerUserRowMapper.class)
    List<ServerUser> getAll();

    @SqlQuery("select * from user where id in (<ids>)")
    @RegisterRowMapper(ServerUserRowMapper.class)
    List<ServerUser> getByIds(List<UUID> ids);

    @SqlQuery("select * from user where id = :id")
    @RegisterRowMapper(ServerUserRowMapper.class)
    Optional<ServerUser> getById(UUID id);

    @SqlQuery("select * from user where name = :name")
    @RegisterRowMapper(ServerUserRowMapper.class)
    Optional<ServerUser> getByName(String name);

    @SqlQuery("select * from user where email = :email")
    @RegisterRowMapper(ServerUserRowMapper.class)
    Optional<ServerUser> getByEmail(String email);

    @Transaction
    @SqlUpdate(
            "insert into user (id, type, permission, name, email, password, modified_at) values"
                    + " (:id, :type, :permission, :name, :email, :password, :modifiedAt)")
    void insert(@BindBean ServerUser user);

    @SqlUpdate("delete from user where id = :id")
    void delete(@BindBean ServerUser user);

    @Transaction
    @SqlUpdate(
            "update user set type = :type, permission = :permission, name = :name, email = :email,"
                    + " password = :password, modified_at = :modifiedAt where id = :id")
    void update(@BindBean ServerUser user);
}
