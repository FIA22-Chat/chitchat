package io.github.chitchat.common.storage.database.dao;

import io.github.chitchat.common.storage.database.dao.mappers.MessageRowMapper;
import io.github.chitchat.common.storage.database.models.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(Message.class)
public interface MessageDAO {
    @SqlQuery("select count(*) from message")
    int count();

    @SqlQuery("select exists(select 1 from message where id = :id)")
    boolean existsById(UUID id);

    @SqlQuery("select exists(select 1 from message where id = :id)")
    boolean exists(@BindBean Message group);

    @SqlQuery("select * from message order by id")
    @RegisterRowMapper(MessageRowMapper.class)
    List<Message> getAll();

    @SqlQuery("select * from message where id in (<ids>) order by id")
    @RegisterRowMapper(MessageRowMapper.class)
    List<Message> getByIds(@BindList("ids") List<UUID> ids);

    @SqlQuery("select * from message where id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    Optional<Message> getById(UUID id);

    @SqlQuery("select * from message where id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    Optional<Message> getByGroupId(UUID id);

    @SqlQuery("select * from message where user_id = :id")
    @RegisterRowMapper(MessageRowMapper.class)
    Optional<Message> getByUserId(UUID id);

    @Transaction
    @SqlUpdate(
            "insert into message (id, user_id, group_id, type, content, modified_at) values"
                    + " (:id, :userId, :groupId, :type, :content, :modifiedAt)")
    void insert(@BindBean Message group);

    @Transaction
    @SqlUpdate("delete from message where id = :id")
    void delete(@BindBean Message group);

    @Transaction
    @SqlUpdate(
            "update message set user_id = :userId, group_id = :groupId, type = :type, content ="
                    + " :content, modified_at = :modifiedAt where id = :id")
    void update(@BindBean Message group);
}
