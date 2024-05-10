package io.github.chitchat.server.models;

import io.github.chitchat.common.storage.database.dao.UserDAO;
import java.util.Optional;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(ServerUser.class)
public interface ServerUserDAO extends UserDAO<ServerUser> {
    @SqlQuery("select * from user where email = :email")
    Optional<ServerUser> getByEmail(String email);

    @Transaction
    @SqlUpdate(
            "insert into user (id, type, permission, name, email, password, modified_at) values"
                    + " (:id, :type, :permission, :name, :email, :password, :modifiedAt)")
    void insert(ServerUser user);

    @Transaction
    @SqlUpdate(
            "update user set type = :type, permission = :permission, name = :name, email = :email,"
                    + " password = :password, modified_at = :modifiedAt where id = :id")
    void update(ServerUser user);
}
