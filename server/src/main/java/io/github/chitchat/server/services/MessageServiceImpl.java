package io.github.chitchat.server.services;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.github.chitchat.common.filter.ProfanityFilter;
import io.github.chitchat.common.filter.SpamFilter;
import io.github.chitchat.common.storage.database.DbUtil;
import io.github.chitchat.common.storage.database.models.inner.PermissionType;
import io.github.chitchat.common.storage.database.service.GroupService;
import io.github.chitchat.common.storage.database.service.MessageService;
import io.github.chitchat.common.storage.database.service.UserGroupService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import io.github.chitchat.server.database.service.ServerUserService;
import io.github.chitchat.service.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class MessageServiceImpl extends MessageServiceGrpc.MessageServiceImplBase {
    private final MessageService messageService;
    private final ServerUserService userService;
    private final GroupService groupService;
    private final UserGroupService userGroupService;
    private final ProfanityFilter profanityFilter;
    private final SpamFilter spamFilter;

    public MessageServiceImpl(
            MessageService messageService,
            ServerUserService userService,
            GroupService groupService,
            UserGroupService userGroupService,
            ProfanityFilter profanityFilter,
            SpamFilter spamFilter) {
        super();
        this.messageService = messageService;
        this.userService = userService;
        this.groupService = groupService;
        this.userGroupService = userGroupService;
        this.profanityFilter = profanityFilter;
        this.spamFilter = spamFilter;
    }

    private static Message convertMessage(
            io.github.chitchat.common.storage.database.models.Message message) {
        if (message == null) return null;

        return Message.newBuilder()
                .setMessageId(message.getId().toString())
                .setUserId(message.getUserId().toString())
                .setGroupId(message.getGroupId().toString())
                .setType(convertMessageType(message.getType()))
                .setContent(ByteString.copyFrom(message.getContent()))
                .setModifiedAt(message.getModifiedAt().toEpochMilli())
                .build();
    }

    private static io.github.chitchat.common.storage.database.models.Message convertMessage(
            Message message) {
        if (message == null) return null;

        return new io.github.chitchat.common.storage.database.models.Message(
                UUIDUtil.uuid(message.getMessageId()),
                UUIDUtil.uuid(message.getUserId()),
                UUIDUtil.uuid(message.getGroupId()),
                convertMessageType(message.getType()),
                message.getContent().toByteArray(),
                Instant.ofEpochMilli(message.getModifiedAt()));
    }

    private static MessageType convertMessageType(
            io.github.chitchat.common.storage.database.models.inner.MessageType type) {
        if (type == null) return null;

        return switch (type) {
            case TEXT -> MessageType.TEXT;
            case MEDIA -> MessageType.MEDIA;
        };
    }

    private static io.github.chitchat.common.storage.database.models.inner.MessageType
            convertMessageType(MessageType type) {
        if (type == null) return null;

        return switch (type) {
            case TEXT -> io.github.chitchat.common.storage.database.models.inner.MessageType.TEXT;
            case MEDIA -> io.github.chitchat.common.storage.database.models.inner.MessageType.MEDIA;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    public void getMessage(
            @NotNull GetMessageRequest request, @NotNull StreamObserver<Message> responseObserver) {
        var message = messageService.get(UUIDUtil.uuid(request.getMessageId()));
        responseObserver.onNext(convertMessage(message.orElse(null)));
        responseObserver.onCompleted();
    }

    @Override
    public void listMessages(
            @NotNull ListMessagesRequest request,
            @NotNull StreamObserver<Message> responseObserver) {
        if (request.hasUserId() && request.hasGroupId()) {
            messageService
                    .getByGroupUser(
                            UUIDUtil.uuid(request.getGroupId()), UUIDUtil.uuid(request.getUserId()))
                    .forEach(message -> responseObserver.onNext(convertMessage(message)));
            responseObserver.onCompleted();
            return;
        }

        if (request.hasGroupId()) {
            messageService
                    .getByGroup(UUIDUtil.uuid(request.getGroupId()))
                    .forEach(message -> responseObserver.onNext(convertMessage(message)));
            responseObserver.onCompleted();
            return;
        }
        if (request.hasUserId()) {
            messageService
                    .getByUser(UUIDUtil.uuid(request.getUserId()))
                    .forEach(message -> responseObserver.onNext(convertMessage(message)));
            responseObserver.onCompleted();
            return;
        }

        responseObserver.onCompleted();
    }

    @Override
    public void sendMessage(
            @NotNull SendMessageRequest request,
            @NotNull StreamObserver<Message> responseObserver) {
        var user = userService.get(UUIDUtil.uuid(request.getUserId()));
        if (user.isEmpty() || !user.get().getPermission().contains(PermissionType.SEND_MESSAGE)) {
            responseObserver.onError(
                    Status.PERMISSION_DENIED
                            .withDescription("User does not have permission to send messages")
                            .asException());
            return;
        }

        var group = groupService.get(UUIDUtil.uuid(request.getGroupId()));
        if (group.isEmpty()) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("Group does not exist").asException());
            return;
        }

        if (!userGroupService.exists(group.get(), user.get())) {
            responseObserver.onError(
                    Status.PERMISSION_DENIED
                            .withDescription("User is not part of the group")
                            .asException());
            return;
        }

        if (spamFilter.isSpamming(user.get())) {
            responseObserver.onError(
                    Status.PERMISSION_DENIED.withDescription("User is spamming").asException());
            return;
        }

        if (profanityFilter.containsProfanities(request.getContent().toStringUtf8())) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("Message contains profanity")
                            .asException());
            return;
        }

        var msg =
                new io.github.chitchat.common.storage.database.models.Message(
                        DbUtil.newId(),
                        UUIDUtil.uuid(request.getUserId()),
                        UUIDUtil.uuid(request.getGroupId()),
                        convertMessageType(request.getType()),
                        request.getContent().toByteArray(),
                        Instant.now());
        try {
            messageService.create(msg);
        } catch (DuplicateItemException e) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS.withDescription("Message already exists").asException());
            return;
        }

        responseObserver.onNext(convertMessage(msg));
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMessage(
            @NotNull DeleteMessageRequest request,
            @NotNull StreamObserver<Empty> responseObserver) {
        var message = messageService.get(UUIDUtil.uuid(request.getMessageId()));
        if (message.isEmpty()) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("Message does not exist").asException());
            return;
        }

        messageService.delete(message.get());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
