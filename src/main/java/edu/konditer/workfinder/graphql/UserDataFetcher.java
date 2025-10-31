package edu.konditer.workfinder.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import edu.konditer.workfinder.service.UserService;
import edu.konditer.workfinder_contracts.dto.UserRequest;
import edu.konditer.workfinder_contracts.dto.UserResponse;

import java.util.List;
import java.util.Map;

@DgsComponent
public class UserDataFetcher {

    private final UserService userService;

    public UserDataFetcher(UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public UserResponse userById(@InputArgument Long id) {
        return userService.findUserById(id);
    }

    @DgsMutation
    public UserResponse createUser(@InputArgument("input") Map<String, Object> input) {
        UserRequest request = new UserRequest(
                (String) input.get("firstName"),
                (String) input.get("lastName"),
                (List<String>) input.get("featuredJobs"));
        return userService.createUser(request);
    }

    @DgsMutation
    public UserResponse updateUser(@InputArgument Long id,
                                   @InputArgument("input") Map<String, Object> input) {
        UserRequest request = new UserRequest(
                (String) input.get("firstName"),
                (String) input.get("lastName"),
                (List<String>) input.get("featuredJobs"));
        return userService.updateUser(id, request);
    }

    @DgsMutation
    public Long deleteUser(@InputArgument Long id) {
        userService.deleteUser(id);
        return id;
    }
}

