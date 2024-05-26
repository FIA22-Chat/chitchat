package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.GroupDAO;
import io.github.chitchat.common.storage.database.dao.GroupDAOImpl;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import org.jdbi.v3.core.Jdbi;

public class GroupService extends CacheableIndexableService<GroupDAO, Group> {

    public GroupService(Jdbi db, int cacheSize) {
        super(new GroupDAOImpl.OnDemand(db), cacheSize);
    }
}
