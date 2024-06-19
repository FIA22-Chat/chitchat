package io.github.chitchat.server.services;

import io.github.chitchat.service.GroupServiceGrpc;

public class GroupServiceImpl extends GroupServiceGrpc.GroupServiceImplBase {
    @Override
    public void getGroup(
            io.github.chitchat.service.GetGroupRequest request,
            io.grpc.stub.StreamObserver<io.github.chitchat.service.Group> responseObserver) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void listGroups(
            io.github.chitchat.service.ListGroupsRequest request,
            io.grpc.stub.StreamObserver<io.github.chitchat.service.ListGroupsResponse>
                    responseObserver) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void createGroup(
            io.github.chitchat.service.CreateGroupRequest request,
            io.grpc.stub.StreamObserver<io.github.chitchat.service.Group> responseObserver) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteGroup(
            io.github.chitchat.service.DeleteGroupRequest request,
            io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
