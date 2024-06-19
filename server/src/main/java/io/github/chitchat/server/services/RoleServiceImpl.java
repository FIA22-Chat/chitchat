package io.github.chitchat.server.services;

import io.github.chitchat.service.RoleServiceGrpc;

public class RoleServiceImpl extends RoleServiceGrpc.RoleServiceImplBase {
    @Override
    public void getRole(
            io.github.chitchat.service.GetRoleRequest request,
            io.grpc.stub.StreamObserver<io.github.chitchat.service.Role> responseObserver) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void listRoles(
            io.github.chitchat.service.ListRolesRequest request,
            io.grpc.stub.StreamObserver<io.github.chitchat.service.ListRolesResponse>
                    responseObserver) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void createRole(
            io.github.chitchat.service.CreateRoleRequest request,
            io.grpc.stub.StreamObserver<io.github.chitchat.service.Role> responseObserver) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteRole(
            io.github.chitchat.service.DeleteRoleRequest request,
            io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {

        throw new UnsupportedOperationException("Not implemented");
    }
}
