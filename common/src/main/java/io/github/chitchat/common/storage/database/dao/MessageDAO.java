package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.common.IIndexableDAO;
import io.github.chitchat.common.storage.database.dao.mappers.MessageRowMapper;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.Message;
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
@RegisterBeanMapper(Message.class)
public abstract class MessageDAO implements IIndexableDAO<UUID, Message> {
    @SqlQuery("select count(*) from message")
    public abstract int count();

    @SqlQuery("select count(*) from message where user_id = :id")
    public abstract int count(@BindBean User user);

    @SqlQuery("select count(*) from message where group_id = :id")
    public abstract int count(@BindBean Group group);

    @SqlQuery("select count(*) from message where group_id = :group.id and user_id = :user.id")
    public abstract int count(@BindBean("group") Group group, @BindBean("user") User user);

    @SqlQuery("select exists(select 1 from message where id = :id)")
    public abstract boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from message where id = :id)")
    public abstract boolean exists(@BindBean Message message);

    @SqlQuery("select * from message")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getAll();

    @SqlQuery("select * from message where id in (<ids>)")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getById(@BindList("ids") List<UUID> ids);

    @SqlQuery("select * from message where id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract Optional<Message> getById(UUID id);

    @SqlQuery("select * from message where group_id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getByGroup(UUID id);

    @SqlQuery("select * from message where group_id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getByGroup(@BindBean Group group);

    @SqlQuery("select * from message where user_id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getByUser(UUID id);

    @SqlQuery("select * from message where user_id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getByUser(@BindBean User user);

    @SqlQuery("select * from message where group_id = :groupId and user_id = :userId")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getByGroupUser(UUID groupId, UUID userId);

    @SqlQuery("select * from message where group_id = :group.id and user_id = :user.id")
    @RegisterRowMapper(MessageRowMapper.class)
    public abstract List<Message> getByGroupUser(
            @BindBean("group") Group group, @BindBean("user") User user);

    @Transaction
    @SqlUpdate(
            "insert into message (id, user_id, group_id, type, content, modified_at) values"
                    + " (:id, :userId, :groupId, :type, :content, :modifiedAt)")
    public abstract void insert(@BindBean Message group);

    @Transaction
    @SqlUpdate("delete from message where id = :id")
    public abstract void delete(@BindBean Message group);

    @Transaction
    @SqlUpdate(
            "update message set user_id = :userId, group_id = :groupId, type = :type, content ="
                    + " :content, modified_at = :modifiedAt where id = :id")
    public abstract void update(@BindBean Message group);
}
