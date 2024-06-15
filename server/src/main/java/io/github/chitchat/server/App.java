package io.github.chitchat.server;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import io.github.chitchat.common.filter.ProfanityFilter;
import io.github.chitchat.common.filter.SpamFilter;
import io.github.chitchat.common.storage.database.models.Group;
import io.github.chitchat.common.storage.database.models.Message;
import io.github.chitchat.common.storage.database.models.Role;
import io.github.chitchat.common.storage.database.service.*;
import io.github.chitchat.common.storage.database.service.channel.ServiceChannel;
import io.github.chitchat.server.database.service.ServerUserService;
import io.github.chitchat.server.database.service.ServerUserSessionService;
import io.github.chitchat.server.services.MessageServiceImpl;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.log4j.Log4j2;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class App implements Runnable {
    private static final int CACHE_SIZE = 1000;

    private final Server server;
    private final Jdbi db;

    private final ServerUserService userService;
    private final ServerUserSessionService userSessionService;
    private final GroupService groupService;
    private final MessageService messageService;
    private final RoleService roleService;
    private final UserGroupService userGroupService;
    private final UserRoleService userRoleService;

    private final ProfanityFilter profanityFilter;
    private final SpamFilter spamFilter;

    public App(int port, Jdbi db) {
        this.server = createServer(port);
        this.db = db;

        var groupChannel = new ServiceChannel<Group>();
        var messageChannel = new ServiceChannel<Message>();
        var roleChannel = new ServiceChannel<Role>();

        this.userService = new ServerUserService(db, CACHE_SIZE);
        this.userSessionService = new ServerUserSessionService(db);
        this.groupService = new GroupService(db, CACHE_SIZE, groupChannel);
        this.messageService = new MessageService(db, CACHE_SIZE, messageChannel);
        this.roleService = new RoleService(db, CACHE_SIZE, roleChannel);
        this.userGroupService = new UserGroupService(db);
        this.userRoleService = new UserRoleService(db);

        this.profanityFilter = Main.loadProfanityFilter();
        this.spamFilter = new SpamFilter();
    }

    @Override
    public void run() {
        log.info("Starting server...");
        server.start().join();
    }

    public void stop() {
        log.info("Stopping server...");
        server.stop().join();
    }

    private @NotNull Server createServer(int port) {
        var grpcService =
                GrpcService.builder()
                        .addService(
                                new MessageServiceImpl(
                                        messageService,
                                        userService,
                                        groupService,
                                        userGroupService,
                                        profanityFilter,
                                        spamFilter))
                        .addService(ProtoReflectionService.newInstance())
                        .enableHealthCheckService(true)
                        .build();

        return Server.builder()
                .http(port)
                .service(grpcService)
                .accessLogWriter(AccessLogWriter.common(), true)
                .build();
    }
}
