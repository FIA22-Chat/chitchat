package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.UserDAO;
import io.github.chitchat.common.storage.database.dao.UserDAOImpl;
import io.github.chitchat.common.storage.database.models.User;
import io.github.chitchat.common.storage.database.service.common.CacheableIndexableService;
import org.jdbi.v3.core.Jdbi;

public class UserService extends CacheableIndexableService<UserDAO, User> {

    public UserService(Jdbi db, int cacheSize) {
        super(new UserDAOImpl.OnDemand(db), cacheSize);
    }
}
