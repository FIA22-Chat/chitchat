package io.github.chitchat.common.database.models;

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

    @SqlQuery("select * from user where email = :email")
    @RegisterBeanMapper(User.class)
    User getByEmail(String email);

    @SqlUpdate(
            "insert into user (id, type, permission, name, email, password, modifiedAt) values"
                    + " (:id, :type, :permission, :name, :email, :password, :modifiedAt)")
    void insert(User user);

    @SqlUpdate("delete from user where id = :id")
    void delete(User user);

    @SqlUpdate(
            "update user set type = :type, permission = :permission, name = :name, email = :email,"
                    + " password = :password, modifiedAt = :modifiedAt where id = :id")
    void update(User user);
}
