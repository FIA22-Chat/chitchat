package io.github.chitchat.server.database.service;

import io.github.chitchat.common.storage.database.service.common.BaseService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import io.github.chitchat.server.database.dao.ServerUserSessionDAO;
import io.github.chitchat.server.database.dao.ServerUserSessionDAOImpl;
import io.github.chitchat.server.database.models.ServerUserSession;
import java.util.Optional;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

public class ServerUserSessionService extends BaseService<ServerUserSessionDAO, ServerUserSession> {
    public ServerUserSessionService(Jdbi db) {
        super(new ServerUserSessionDAOImpl.OnDemand(db));
    }

    public Optional<ServerUserSession> getByUserId(UUID serverId) {
        return dao.get(serverId);
    }

    public Optional<ServerUserSession> getByToken(String token) {
        return dao.getByToken(token);
    }

    @Override
    public void create(@NotNull ServerUserSession value) throws DuplicateItemException {
        dao.insert(value);
    }

    @Override
    public void update(@NotNull ServerUserSession value) {
        dao.update(value);
    }

    @Override
    public void delete(@NotNull ServerUserSession value) {
        dao.delete(value);
    }
}
