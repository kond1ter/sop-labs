package edu.konditer.workfinder.assemblres;

import edu.konditer.workfinder.controllers.UserController;
import edu.konditer.workfinder.controllers.VacancyController;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VacancyModelAssembler implements RepresentationModelAssembler<VacancyResponse, EntityModel<VacancyResponse>> {

    @NotNull
    @Override
    public EntityModel<VacancyResponse> toModel(@NotNull VacancyResponse vacancy) {
        return EntityModel.of(vacancy,
                linkTo(methodOn(VacancyController.class)
                        .getVacancyById(vacancy.getId()))
                        .withSelfRel(),
                linkTo(methodOn(UserController.class)
                        .getUserById(vacancy.getAuthor().getId()))
                        .withRel("author"),
                linkTo(methodOn(VacancyController.class)
                        .getAllVacancies(null, 0, 10))
                        .withRel("collection")
        );
    }
}
