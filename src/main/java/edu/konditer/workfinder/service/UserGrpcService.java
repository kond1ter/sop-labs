package edu.konditer.workfinder.service;

import edu.konditer.workfinder.User;
import edu.konditer.workfinder.UserServiceGrpc;
import edu.konditer.workfinder.UsersRequest;
import edu.konditer.workfinder.UsersResponse;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;

    protected UserGrpcService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void getUsers(UsersRequest request, StreamObserver<UsersResponse> responseObserver) {
        List<UserResponse> users = userService.findAll();

        List<User> grpcUsers = users.stream()
                .map(user -> User.newBuilder()
                        .setId(user.getId().toString())
                        .setFirstName(user.getFirstName())
                        .setLastName(user.getLastName())
                        .addAllFeaturedJobs(user.getFeaturedJobs())
                        .build()
                )
                .toList();

        UsersResponse response = UsersResponse.newBuilder()
                .addAllUsers(grpcUsers)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

