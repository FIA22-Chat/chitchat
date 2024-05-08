package io.github.chitchat.common.database;

import io.github.chitchat.common.database.models.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface UserDAO {


    @SqlUpdate("insert into User(username, email, password) values(:username, :email, :password)")
    void insertUser(@Bind("username") String username, @Bind("email") String email, @Bind("password") String password);

    @SqlUpdate("delete from User where userID = :id")
    void deleteUser(@Bind("id") int id);

    @SqlQuery("select userID, email, password from User where userID = :id")
    User findUsername(@Bind("id") int id);

}
