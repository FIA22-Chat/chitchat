package io.github.chitchat.common.database;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;

public interface MessageDAO {
    @SqlUpdate("insert into message(messagetype, from, to, content, datetime) values(:messagetype, :from, :to, :content, :datetime)")
    void insert(@Bind("messagetype") String messagetype, @Bind ("from") int from, @Bind ("to") int to, @Bind ("content") String content, @Bind ("datetime") LocalDateTime datetime);

    @SqlUpdate("delete from message where messageID = :id")
    void delete(@Bind("id") int id);
}
