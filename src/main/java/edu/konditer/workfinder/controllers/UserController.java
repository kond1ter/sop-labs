package edu.konditer.workfinder.controllers;

import edu.konditer.workfinder_contracts.dto.UserRequest;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.endpoints.UserApi;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    @Override
    public UserResponse getUserById(Long id) {
        return null;
    }

    @Override
    public UserResponse createUser(@Valid UserRequest request) {
        return null;
    }

    @Override
    public UserResponse updateUser(Long id, @Valid UserRequest request) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
