package io.github.chitchat.common.storage.database.service;

import io.github.chitchat.common.storage.database.dao.MessageDAO;
import io.github.chitchat.common.storage.database.dao.MessageDAOImpl;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.service.common.IndexableService;
import org.jdbi.v3.core.Jdbi;

public class MessageService extends IndexableService<MessageDAO, Message> {

    public MessageService(Jdbi db, int cacheSize) {
        super(new MessageDAOImpl.OnDemand(db), cacheSize);
    }
}
