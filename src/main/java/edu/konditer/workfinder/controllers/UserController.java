package edu.konditer.workfinder.controllers;

import edu.konditer.workfinder.assemblres.UserModelAssembler;
import edu.konditer.workfinder.assemblres.VacancyModelAssembler;
import edu.konditer.workfinder.service.UserService;
import edu.konditer.workfinder.service.VacancyService;
import edu.konditer.workfinder_contracts.dto.UserRequest;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import edu.konditer.workfinder_contracts.endpoints.UserApi;
import jakarta.validation.Valid;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;

    public UserController(UserService userService, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.userModelAssembler = userModelAssembler;
    }

    @Override
    public EntityModel<UserResponse> getUserById(Long id) {
        UserResponse user = userService.findUserById(id);
        return userModelAssembler.toModel(user);
    }

    @Override
    public ResponseEntity<EntityModel<UserResponse>> createUser(@Valid UserRequest request) {
        UserResponse createdUser = userService.createUser(request);
        EntityModel<UserResponse> entityModel = userModelAssembler.toModel(createdUser);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<UserResponse> updateUser(Long id, @Valid UserRequest request) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return userModelAssembler.toModel(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }
}
