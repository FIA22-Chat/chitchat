package auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import io.github.chitchat.server.auth.Authentication;
import io.github.chitchat.server.auth.exceptions.AuthenticationFailedException;
import io.github.chitchat.server.auth.exceptions.TemporarilyBlockedException;
import io.github.chitchat.server.database.models.ServerUser;
import io.github.chitchat.server.database.service.ServerUserService;
import io.github.chitchat.server.database.service.ServerUserSessionService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Log4j2
public class AuthTest {
    private static ServerUserService userService;
    private static ServerUserSessionService userSession;
    private static Authentication authentication;

    @BeforeAll
    protected static void initService() {
        var db = Common.createDB("auth.db");
        db.useHandle(
                handle -> {
                    handle.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS user
                                    (
                                        -- Id is expected to be unique and chronologically sortable (e.g. UUID v7)
                                        -- No need for a index, SQLite automatically creates a index for the primary key
                                        id          blob    NOT NULL UNIQUE PRIMARY KEY,
                                        type        integer NOT NULL,
                                        -- Permission contains bitflags where each bit represents a permission scope
                                        -- Refer to the Java implementation for more details
                                        permission  integer NOT NULL,
                                        name        text    NOT NULL,
                                        email       text    NOT NULL,
                                        password    text    NOT NULL,
                                        -- Modified at should be updated when a change is made to the user record
                                        -- It is expected to be a Unix timestamp in milliseconds
                                        modified_at text    NOT NULL
                                    );
                                    """);
                    handle.execute(
                            """
                                    CREATE TABLE IF NOT EXISTS user_session
                                    (
                                        user_id    blob NOT NULL REFERENCES user (id),
                                        -- A temporary token that is used to authenticate the user
                                        token      text NOT NULL,
                                        expires_at text NOT NULL
                                    );
                                    """);
                });

        userService = new ServerUserService(db, 100);
        userSession = new ServerUserSessionService(db);
        authentication = new Authentication(userService, userSession);
    }

    @Test
    public void registerUserTest() throws DuplicateItemException {
        ServerUser testUser =
                authentication.registerUser("username", "example@example.com", "password");
        assertNotNull(testUser);
        assertTrue(userService.exists(testUser.getId()));
    }

    @Test
    public void createSessionTest()
            throws DuplicateItemException,
                    TemporarilyBlockedException,
                    AuthenticationFailedException {
        ServerUser testUser =
                authentication.registerUser("username2", "example2@example.com", "password");
        assertNotNull(testUser);
        assertTrue(userService.exists(testUser.getId()));

        var session = authentication.createSession(testUser, "password");
        assertNotNull(session);
        assertTrue(userSession.getByUserId(session.getUserId()).isPresent());
    }
}
