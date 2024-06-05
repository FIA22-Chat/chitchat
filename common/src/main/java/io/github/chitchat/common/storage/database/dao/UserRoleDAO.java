package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.common.IBaseDAO;
import io.github.chitchat.common.storage.database.dao.mappers.RoleRowMapper;
import io.github.chitchat.common.storage.database.dao.mappers.UserRoleRowMapper;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.Role;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.models.UserRole;
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
@RegisterBeanMapper(UserRole.class)
public abstract class UserRoleDAO implements IBaseDAO<UserRole> {
    /**
     * Count the number of user role associations.
     *
     * @return the number of user role associations.
     */
    @SqlQuery("select count(*) from user_role")
    public abstract int count();

    /**
     * Count the total number of roles that a user has across all groups.
     *
     * @param user the user to count the number of roles for.
     * @return the total number of roles that the user has.
     */
    @SqlQuery("select count(*) from user_role where user_id = :id")
    public abstract int count(@BindBean User user);

    /**
     * Count the number of roles that a user has in a group.
     *
     * @param user the user to count the number of roles for.
     * @param group the group context to count the number of roles for.
     * @return the number of roles that the user has in the group.
     */
    @SqlQuery(
            "select count(*) from user_role inner join role on user_role.role_id = role.id where"
                    + " user_role.user_id = :user.id and role.group_id = :group.id")
    public abstract int count(@BindBean("user") User user, @BindBean("group") Group group);

    /**
     * Check if a user has a role.
     *
     * @param userId the user id to check.
     * @param roleId the role id to check.
     * @return true if the user has the role, false otherwise.
     */
    @SqlQuery(
            "select exists(select 1 from user_role where user_id = :userId and role_id = :roleId)")
    public abstract boolean exists(UUID userId, UUID roleId);

    /**
     * Check if a user has a role.
     *
     * @param user the user to check.
     * @param role the role to check.
     * @return true if the user has the role, false otherwise.
     */
    @SqlQuery(
            "select exists(select 1 from user_role where user_id = :user.id and role_id ="
                    + " :role.id)")
    public abstract boolean exists(@BindBean("user") User user, @BindBean("role") Role role);

    /**
     * Check if a user has a role.
     *
     * @param UserRole the user role to check.
     * @return true if the user has the role, false otherwise.
     */
    @SqlQuery(
            "select exists(select 1 from user_role where user_id = :userId and role_id ="
                    + " :roleId)")
    public abstract boolean exists(@BindBean UserRole UserRole);

    /**
     * Get all the user roles associations.
     *
     * @return all the user roles associations.
     */
    @SqlQuery("select * from user_role")
    @RegisterRowMapper(UserRoleRowMapper.class)
    public abstract List<UserRole> getAll();

    /**
     * Get the roles that multiple users have globally.
     *
     * @param userIds the user ids to get the roles for.
     * @return the roles that the users have.
     */
    @SqlQuery(
            "select role.* from user_role inner join role on user_role.role_id = role.id where"
                    + " user_id in (<userIds>)")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getUserRoles(@BindList("userIds") List<UUID> userIds);

    /**
     * Get the roles that a user has globally.
     *
     * @param userId the user to get the roles for.
     * @return the roles that the user has.
     */
    @SqlQuery(
            "select role.* from user_role inner join role on user_role.role_id = role.id where"
                    + " user_id = :userId")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getUserRoles(UUID userId);

    /**
     * Get the roles that a user has globally.
     *
     * @param user the user to get the roles for.
     * @return the roles that the user has.
     */
    @SqlQuery(
            "select role.* from user_role inner join role on user_role.role_id = role.id where"
                    + " user_id = :id")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getUserRoles(@BindBean User user);

    /**
     * Get the roles that a user has within a group.
     *
     * @param groupId the group context to get the roles for.
     * @param userId the user id to get the roles for.
     * @return the roles that the user has within the group.
     */
    @SqlQuery(
            "select role.* from user_role inner join role on user_role.role_id = role.id where"
                    + " role.group_id = :groupId and user_role.user_id = :userId")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getGroupUserRoles(UUID groupId, UUID userId);

    /**
     * Get the roles that a user has within a group.
     *
     * @param group the group context to get the roles for.
     * @param user the user to get the roles for.
     * @return the roles that the user has within the group.
     */
    @SqlQuery(
            "select role.* from user_role inner join role on user_role.role_id = role.id where"
                    + " role.group_id = :group.id and user_role.user_id = :user.id")
    @RegisterRowMapper(RoleRowMapper.class)
    public abstract List<Role> getGroupUserRoles(
            @BindBean("group") Group group, @BindBean("user") User user);

    @Transaction
    @SqlUpdate(
            "insert into user_role (user_id, role_id, modified_at) values (:userId, :roleId,"
                    + " :modifiedAt)")
    public abstract void insert(@BindBean UserRole UserRole);

    @Transaction
    @SqlUpdate(
            "insert into user_role (user_id, role_id, modified_at) values (:user.id, :role.id,"
                    + " :modifiedAt)")
    public abstract void insert(
            @BindBean("user") User user, @BindBean("role") Role role, Instant modifiedAt);

    @Transaction
    @SqlUpdate("delete from user_role where user_id = :userId and role_id = :roleId")
    public abstract void delete(@BindBean UserRole UserRole);

    @Transaction
    @SqlUpdate("delete from user_role where user_id = :user.id and role_id = :role.id")
    public abstract void delete(@BindBean("user") User user, @BindBean("role") Role role);

    @Transaction
    @SqlUpdate(
            "update user_role set user_id = :userId, role_id = :roleId, modified_at = :modifiedAt"
                    + " where user_id = :userId")
    public abstract void update(@BindBean UserRole UserRole);

    @Transaction
    @SqlUpdate(
            "update user_role set user_id = :user.id, role_id = :role.id, modified_at = :modifiedAt"
                    + " where user_id = :user.id")
    public abstract void update(
            @BindBean("user") User user, @BindBean("role") Role role, Instant modifiedAt);
}
