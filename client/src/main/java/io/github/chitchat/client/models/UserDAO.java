package io.github.chitchat.client.models;

import io.github.chitchat.common.storage.database.models.User;
import java.util.List;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDAO {
    @SqlQuery("select count(*) from user")
    int count();

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean existsById(int id);

    @SqlQuery("select exists(select 1 from user where id = :id)")
    boolean exists(User user);

    @SqlQuery("select * from user order by id")
    @RegisterBeanMapper(User.class)
    List<User> getAll();

    @SqlQuery("select * from user where id = :id")
    @RegisterBeanMapper(User.class)
    User getById(int id);

    @SqlQuery("select * from user where id in (<ids>)")
    @RegisterBeanMapper(User.class)
    List<User> getByIds(List<Integer> ids);

    @SqlQuery("select * from user where name = :name")
    @RegisterBeanMapper(User.class)
    User getByName(String name);

    @SqlUpdate(
            "insert into user (id, type, permission, name) values (:id, :type, :permission, :name)")
    void insert(User user);

    @SqlUpdate("delete from user where id = :id")
    void delete(User user);

    @SqlUpdate(
            "update user set type = :type, permission = :permission, name = :name where id = :id")
    void update(User user);
}
