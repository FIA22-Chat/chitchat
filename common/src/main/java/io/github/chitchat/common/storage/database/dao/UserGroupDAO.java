package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(User.class)
public interface UserGroupDAO {

    @Transaction
    @SqlUpdate(
            "insert into user_group (user_id, group_id, modified_at) values (:user.id, :group.id,"
                    + " :modified_at)")
    void insert(@BindBean Group group, @BindBean User user);

    @Transaction
    @SqlUpdate("delete from user_group where user_id = :user.id")
    void delete(@BindBean User user);

    @Transaction
    @SqlUpdate(
            "update user_group set user_id = :user.id, group_id = :group.id, modified_at ="
                    + " :modified_at")
    void update(@BindBean User user);
}