package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.GroupDAO;
import io.github.chitchat.common.storage.database.dao.UserDAO;
import io.github.chitchat.common.storage.database.dao.UserGroupDAO;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.User;
import org.jdbi.v3.core.Jdbi;

public class GroupService {

    private final Jdbi db;

    public GroupService(Jdbi db)
    {
        this.db = db;
    }

    public boolean addToGroup(Group group, User user)
    {
        if (!db.onDemand(GroupDAO.class).exists(group)) {
            throw new RuntimeException("GroupDAO");
        }

        if (!db.onDemand(UserDAO.class).exists(user)) {
            throw new RuntimeException("UserDAO");
        }

        db.onDemand(UserGroupDAO.class).insert(group, user);
        return true;

    }
}
