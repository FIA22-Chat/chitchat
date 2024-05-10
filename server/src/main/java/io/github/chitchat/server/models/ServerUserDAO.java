package io.github.chitchat.server.models;

import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface ServerUserDAO {
    @SqlQuery("select count(*) from user")
    int count();

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean existsById(int id);

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean exists(ServerUser user);

    @SqlQuery("select * from user order by id")
    @RegisterBeanMapper(ServerUser.class)
    List<ServerUser> getAll();

    @SqlQuery("select * from user where id = :id")
    @RegisterBeanMapper(ServerUser.class)
    ServerUser getById(int id);

    @SqlQuery("select * from user where id in (<ids>)")
    @RegisterBeanMapper(ServerUser.class)
    List<ServerUser> getByIds(List<Integer> ids);

    @SqlQuery("select * from user where name = :name")
    @RegisterBeanMapper(ServerUser.class)
    ServerUser getByName(String name);

    @SqlQuery("select * from user where email = :email")
    @RegisterBeanMapper(ServerUser.class)
    ServerUser getByEmail(String email);

    @SqlUpdate(
            "insert into user (id, type, permission, name, email, password, modified_at) values"
                    + " (:id, :type, :permission, :name, :email, :password, :modifiedAt)")
    void insert(ServerUser user);

    @SqlUpdate("delete from user where id = :id")
    void delete(ServerUser user);

    @SqlUpdate(
            "update user set type = :type, permission = :permission, name = :name, email = :email,"
                    + " password = :password, modified_at = :modifiedAt where id = :id")
    void update(ServerUser user);
}
