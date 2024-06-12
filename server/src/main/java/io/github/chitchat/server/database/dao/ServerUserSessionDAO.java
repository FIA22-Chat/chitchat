package io.github.chitchat.server.database.dao;

import io.github.chitchat.common.storage.database.dao.common.IBaseDAO;
import io.github.chitchat.server.database.dao.mappers.ServerUserSessionRowMapper;
import io.github.chitchat.server.database.models.ServerUserSession;
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
@RegisterBeanMapper(ServerUserSession.class)
public abstract class ServerUserSessionDAO implements IBaseDAO<ServerUserSession> {
    @SqlQuery("select count(*) from user_session")
    public abstract int count();

    @SqlQuery("select exists(select 1 from user_session where user_id = :userId)")
    public abstract boolean exists(UUID userId);

    @SqlQuery("select exists(select 1 from user_session where user_id = :userId)")
    public abstract boolean exists(@BindBean ServerUserSession userSession);

    @SqlQuery("select * from user_session")
    @RegisterRowMapper(ServerUserSessionRowMapper.class)
    public abstract List<ServerUserSession> getAll();

    @SqlQuery("select * from user_session where user_id in (<userIds>)")
    @RegisterRowMapper(ServerUserSessionRowMapper.class)
    public abstract List<ServerUserSession> get(@BindList("userIds") List<UUID> userIds);

    @SqlQuery("select * from user_session where user_id = :userId")
    @RegisterRowMapper(ServerUserSessionRowMapper.class)
    public abstract Optional<ServerUserSession> get(UUID userId);

    @SqlQuery("select * from user_session where token = :token")
    @RegisterRowMapper(ServerUserSessionRowMapper.class)
    public abstract Optional<ServerUserSession> getByToken(String token);

    @Transaction
    @SqlUpdate(
            "insert into user_session (user_id, token, expires_at) values"
                    + " (:userId, :token, :expiresAt)")
    public abstract void insert(@BindBean ServerUserSession userSession);

    @Transaction
    @SqlUpdate("delete from user_session where user_id = :userId")
    public abstract void delete(@BindBean ServerUserSession userSession);

    @Transaction
    @SqlUpdate(
            "update user_session set user_id = :userId, token = :token, expires_at = :expiresAt"
                    + " where user_id = :userId")
    public abstract void update(@BindBean ServerUserSession userSession);
}
