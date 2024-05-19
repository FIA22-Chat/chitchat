package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.RoleDAO;
import io.github.chitchat.common.storage.database.dao.RoleDAOImpl;
import io.github.chitchat.common.storage.database.models.Role;
import io.github.chitchat.common.storage.database.service.common.IndexableService;
import org.jdbi.v3.core.Jdbi;

public class RoleService extends IndexableService<RoleDAO, Role> {

    public RoleService(Jdbi db, int cacheSize) {
        super(new RoleDAOImpl.OnDemand(db), cacheSize);
    }
}
