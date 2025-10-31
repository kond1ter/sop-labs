package edu.konditer.workfinder.assemblres;

import edu.konditer.workfinder.controllers.UserController;
import edu.konditer.workfinder.controllers.VacancyController;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    @NotNull
    @Override
    public EntityModel<UserResponse> toModel(@NotNull UserResponse user) {
        return EntityModel.of(user,
                linkTo(methodOn(UserController.class)
                        .getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(VacancyController.class)
                        .getAllVacancies(user.getId(), 0, 10)).withRel("vacancies")
        );
    }
}
