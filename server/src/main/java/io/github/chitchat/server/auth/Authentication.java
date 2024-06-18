package io.github.chitchat.server.auth;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.models.inner.UserType;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import io.github.chitchat.server.auth.exceptions.AuthenticationFailedException;
import io.github.chitchat.server.auth.exceptions.TemporarilyBlockedException;
import io.github.chitchat.server.database.models.ServerUser;
import io.github.chitchat.server.database.models.ServerUserSession;
import io.github.chitchat.server.database.service.ServerUserService;
import io.github.chitchat.server.database.service.ServerUserSessionService;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Log4j2
public class Authentication {
    private static final int ITERATIONS = 10;
    private static final int MEMORY = 65536;
    private static final int PARALLELISM = 1;

    private static final int TOKEN_LENGTH = 256;
    private static final int FAILED_ATTEMPTS_THRESHOLD = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    private static final ThreadLocal<SecureRandom> RANDOM =
            ThreadLocal.withInitial(SecureRandom::new);
    private static final EnumSet<PermissionType> DEFAULT_PERMISSIONS =
            EnumSet.of(
                    PermissionType.DELETE_MESSAGE,
                    PermissionType.EDIT_MESSAGE,
                    PermissionType.SEND_MESSAGE);

    private final Argon2 encoder;
    private final ServerUserService serverUserService;
    private final ServerUserSessionService serverUserSessionService;

    private final LoadingCache<ServerUser, Integer> temporarilyBlockedUsers;

    public Authentication(
            ServerUserService serverUserService,
            ServerUserSessionService serverUserSessionService) {
        this.serverUserService = serverUserService;
        this.serverUserSessionService = serverUserSessionService;
        this.encoder = Argon2Factory.create();

        this.temporarilyBlockedUsers =
                Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(5)).build(_ -> 0);
    }

    public ServerUser registerUser(String name, String email, String password)
            throws DuplicateItemException {
        if (serverUserService.getByName(name).isPresent()) {
            log.trace("User with name: {} already exists", name);
            throw new DuplicateItemException("User with name: " + name + " already exists");
        }
        if (serverUserService.getByEmail(email).isPresent()) {
            log.trace("User with email: {} already exists", email);
            throw new DuplicateItemException("User with email: " + email + " already exists");
        }

        var user =
                new ServerUser(
                        DbUtil.newId(),
                        UserType.USER,
                        DEFAULT_PERMISSIONS,
                        name,
                        email,
                        encoder.hash(ITERATIONS, MEMORY, PARALLELISM, password.getBytes()),
                        Instant.now());

        serverUserService.create(user);
        return user;
    }

    public ServerUserSession createSessionWithName(
            @NotNull String name, @NotNull String email, @NotNull String password)
            throws DuplicateItemException,
                    TemporarilyBlockedException,
                    AuthenticationFailedException {
        var user = serverUserService.getByName(name);
        if (user.isEmpty()) {
            log.trace("User with name: {} does not exist", name);
            throw new NullPointerException("User with name: " + name + " does not exist");
        }

        return createSession(user.get(), password);
    }

    public ServerUserSession createSessionWithEmail(@NotNull String email, @NotNull String password)
            throws DuplicateItemException,
                    TemporarilyBlockedException,
                    AuthenticationFailedException {
        var user = serverUserService.getByEmail(email);
        if (user.isEmpty()) {
            log.trace("User with email: {} does not exist", email);
            throw new NullPointerException("User with email: " + email + " does not exist");
        }

        return createSession(user.get(), password);
    }

    public ServerUserSession createSessionWithId(@NotNull UUID id, @NotNull String password)
            throws DuplicateItemException,
                    TemporarilyBlockedException,
                    AuthenticationFailedException {
        var user = serverUserService.get(id);
        if (user.isEmpty()) {
            log.trace("User with id: {} does not exist", id);
            throw new NullPointerException("User with id: " + id + " does not exist");
        }

        return createSession(user.get(), password);
    }

    public @Nullable ServerUserSession createSession(
            @NotNull ServerUser user, @NotNull String password)
            throws DuplicateItemException,
                    TemporarilyBlockedException,
                    AuthenticationFailedException {
        var currentUserBlockedCount = temporarilyBlockedUsers.get(user);
        if (currentUserBlockedCount >= FAILED_ATTEMPTS_THRESHOLD) {
            log.trace("User with id: {} has been previously temporarily blocked", user.getId());
            throw new TemporarilyBlockedException("User has been previously temporarily blocked");
        }

        if (!encoder.verify(user.getPassword(), password.getBytes())) {
            log.trace("User with id: {} failed to authenticate", user.getId());
            temporarilyBlockedUsers.put(user, currentUserBlockedCount + 1);
            throw new AuthenticationFailedException("Invalid password");
        }

        return createSession(user);
    }

    private @NotNull ServerUserSession createSession(@NotNull ServerUser user)
            throws DuplicateItemException {
        // Fill a byte array with random bytes
        var randBytes = new byte[TOKEN_LENGTH];
        RANDOM.get().nextBytes(randBytes);

        // Encode the random bytes to a base64 string
        var token = Base64.getEncoder().encodeToString(randBytes);
        var session =
                new ServerUserSession(user.getId(), token, Instant.now().plus(BLOCK_DURATION));

        serverUserSessionService.create(session);
        return session;
    }
}
