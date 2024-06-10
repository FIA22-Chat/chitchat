package io.github.chitchat.server;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.server.logging.AccessLogWriter;
import io.github.chitchat.server.services.MessageServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.jdbi.v3.core.Jdbi;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class App implements Runnable {
    private final Jdbi db;
    private final Server server;

    public App(int port, Jdbi db) {
        this.db = db;
        this.server = createServer(port);
    }

    @Override
    public void run() {
        log.info("Starting server on {}...", server.activePorts());
        server.start().join();
    }

    public void stop() {
        log.info("Stopping server...");
        server.stop().join();
    }

    private @NotNull Server createServer(int port) {
        var grpcService = GrpcService.builder().addService(new MessageServiceImpl()).build();

        return Server.builder()
                .http(port)
                .service(grpcService)
                .accessLogWriter(AccessLogWriter.common(), true)
                .build();
    }
}
