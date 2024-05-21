package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.common.IBaseDAO;
import io.github.chitchat.common.storage.database.dao.mappers.GroupRowMapper;
import io.github.chitchat.common.storage.database.dao.mappers.UserGroupRowMapper;
import io.github.chitchat.common.storage.database.dao.mappers.UserRowMapper;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.UserGroup;
import java.time.Instant;
import java.util.List;
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
@RegisterBeanMapper(UserGroup.class)
public abstract class UserGroupDAO implements IBaseDAO<UserGroup> {
    /**
     * Count the number of user groups associations.
     *
     * @return the number of user groups associations.
     */
    @SqlQuery("select count(*) from user_group")
    public abstract int count();

    /**
     * Count the number of users that are part of a group.
     *
     * @param group the group to count the number of users for.
     * @return the number of users that are part of the group.
     */
    @SqlQuery("select count(*) from user_group where group_id = :id")
    public abstract int count(@BindBean Group group);

    /**
     * Count the number groups that a user is part of.
     *
     * @param user the user to count the number of groups for.
     * @return the number of groups that the user is part of.
     */
    @SqlQuery("select count(*) from user_group where user_id = :id")
    public abstract int count(@BindBean User user);

    @SqlQuery(
            "select exists(select 1 from user_group where group_id = :groupId and user_id ="
                    + " :userId)")
    public abstract boolean exists(UUID groupId, UUID userId);

    @SqlQuery(
            "select exists(select 1 from user_group where group_id = :group.id and user_id ="
                    + " :user.id)")
    public abstract boolean exists(@BindBean("group") Group group, @BindBean("user") User user);

    @SqlQuery(
            "select exists(select 1 from user_group where group_id = :groupId and user_id ="
                    + " :userId)")
    public abstract boolean exists(@BindBean UserGroup userGroup);

    @SqlQuery("select * from user_group")
    @RegisterRowMapper(UserGroupRowMapper.class)
    public abstract List<UserGroup> getAll();

    @SqlQuery("select * from user_group where user_id in (<userIds>)")
    @RegisterRowMapper(UserGroupRowMapper.class)
    public abstract List<UserGroup> getByUserId(@BindList("userIds") List<UUID> userIds);

    @SqlQuery("select * from user_group where user_id = :userId")
    @RegisterRowMapper(UserGroupRowMapper.class)
    public abstract List<UserGroup> getByUserId(UUID userId);

    @SqlQuery("select * from user_group where group_id in (<groupIds>)")
    @RegisterRowMapper(UserGroupRowMapper.class)
    public abstract List<UserGroup> getByGroupId(@BindList("groupIds") List<UUID> groupIds);

    @SqlQuery("select * from user_group where group_id = :groupId")
    @RegisterRowMapper(UserGroupRowMapper.class)
    public abstract List<UserGroup> getByGroupId(UUID groupId);

    /**
     * Get the users that are part of a group.
     *
     * @param groupId the group id to get the users for.
     * @return the users that are part of the group.
     */
    @SqlQuery(
            "select user.* from user inner join user_group on user.id = user_group.user_id where"
                    + " user_group.group_id = :groupId")
    @RegisterRowMapper(UserRowMapper.class)
    public abstract List<User> getGroupUsers(UUID groupId);

    /**
     * Get the users that are part of a group.
     *
     * @param group the group to get the users for.
     * @return the users that are part of the group.
     */
    @SqlQuery(
            "select user.* from user inner join user_group on user.id = user_group.user_id where"
                    + " user_group.group_id = :id")
    @RegisterRowMapper(UserRowMapper.class)
    public abstract List<User> getGroupUsers(@BindBean Group group);

    /**
     * Get the groups that a user is part of.
     *
     * @param userId the user id to get the groups for.
     * @return the groups that the user is part of.
     */
    @SqlQuery(
            "select \"group\".* from \"group\" inner join user_group on \"group\".id ="
                    + " user_group.group_id where user_group.user_id = :userId")
    @RegisterRowMapper(GroupRowMapper.class)
    public abstract List<Group> getUserGroups(UUID userId);

    /**
     * Get the groups that a user is part of.
     *
     * @param user the user to get the groups for.
     * @return the groups that the user is part of.
     */
    @SqlQuery(
            "select \"group\".* from \"group\" inner join user_group on \"group\".id ="
                    + " user_group.group_id where user_group.user_id = :id")
    @RegisterRowMapper(GroupRowMapper.class)
    public abstract List<Group> getUserGroups(@BindBean User user);

    @Transaction
    @SqlUpdate(
            "insert into user_group (user_id, group_id, modified_at) values (:user.id, :group.id,"
                    + " :modifiedAt)")
    @RegisterBeanMapper(UserGroup.class)
    public abstract void insert(
            @BindBean("group") Group group, @BindBean("user") User user, Instant modifiedAt);

    @Transaction
    @SqlUpdate(
            "insert into user_group (user_id, group_id, modified_at) values (:userId, :groupId,"
                    + " :modifiedAt)")
    public abstract void insert(@BindBean UserGroup userGroup);

    @Transaction
    @SqlUpdate("delete from user_group where group_id = :group.id and user_id in (<userIds>)")
    public abstract void delete(
            @BindBean("group") Group group, @BindList("userIds") List<UUID> userIds);

    @Transaction
    @SqlUpdate("delete from user_group where group_id = :group.id and user_id = :user.id")
    public abstract void delete(@BindBean("group") Group group, @BindBean("user") User user);

    @Transaction
    @SqlUpdate("delete from user_group where group_id = :groupId and user_id = :userId")
    public abstract void delete(@BindBean UserGroup userGroup);

    @Transaction
    @SqlUpdate(
            "update user_group set user_id = :user.id, group_id = :group.id, modified_at ="
                    + " :modifiedAt where group_id = :group.id and user_id = :user.id")
    public abstract void update(
            @BindBean("group") Group group, @BindBean("user") User user, Instant modifiedAt);

    @Transaction
    @SqlUpdate(
            "update user_group set user_id = :user.id, group_id = :group.id, modified_at ="
                    + " :modifiedAt where group_id = :groupId and user_id = :userId")
    public abstract void update(@BindBean UserGroup userGroup);
}
