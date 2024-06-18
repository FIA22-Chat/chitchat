package auth;

import io.github.chitchat.common.storage.database.service.UserService;
import io.github.chitchat.server.auth.Authentication;
import io.github.chitchat.server.database.models.ServerUser;
import io.github.chitchat.server.database.service.ServerUserService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log4j2
public class AuthTest
{
    private final ServerUserService userService = initService("userServiceAuthTests.db");
    private final Authentication authentication = new Authentication(userService);

    protected @NotNull ServerUserService initService(String name) {
        var db = Common.createDB(name);
        db.useHandle(
                handle ->
                        handle.execute(
                                """
                                        CREATE TABLE IF NOT EXISTS user ( id         blob NOT NULL UNIQUE
                                        PRIMARY KEY,    type       integer NOT NULL,    permission
                                        integer NOT NULL,    name       text    NOT NULL)
                                        """));
        return new ServerUserService(db, 10);
    }

    @Test
    public void getSignUpTest()
    {
        ServerUser testUser = authentication.getSignUp("username",  "useremail", "password", "rePassword");

        assertNotNull(testUser);


    }
    @Test
    public void getSignInTest()
    {

    }
    @Test
    public void createAuth()
    {

    }
}
