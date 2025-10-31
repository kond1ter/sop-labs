package edu.konditer.workfinder.controllers;

import edu.konditer.workfinder.assemblres.VacancyModelAssembler;
import edu.konditer.workfinder.service.VacancyService;
import edu.konditer.workfinder_contracts.dto.PagedResponse;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.dto.VacancyRequest;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import edu.konditer.workfinder_contracts.endpoints.VacancyApi;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacancyController implements VacancyApi {

    private final VacancyService vacancyService;
    private final VacancyModelAssembler vacancyModelAssembler;
    private final PagedResourcesAssembler<VacancyResponse> pagedResourcesAssembler;

    public VacancyController(VacancyService vacancyService, VacancyModelAssembler vacancyModelAssembler, PagedResourcesAssembler<VacancyResponse> pagedResourcesAssembler) {
        this.vacancyService = vacancyService;
        this.vacancyModelAssembler = vacancyModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public EntityModel<VacancyResponse> getVacancyById(Long id) {
        VacancyResponse vacancy = vacancyService.findVacancyById(id);
        return vacancyModelAssembler.toModel(vacancy);
    }

    @Override
    public PagedModel<EntityModel<VacancyResponse>> getAllVacancies(Long userId, int page, int size) {
        PagedResponse<VacancyResponse> pagedResponse = vacancyService.findAllVacancies(userId, page, size);

        Page<VacancyResponse> vacancyPage = new PageImpl<>(
                pagedResponse.content(),
                PageRequest.of(pagedResponse.pageNumber(), pagedResponse.pageSize()),
                pagedResponse.totalElements()
        );

        return pagedResourcesAssembler.toModel(vacancyPage, vacancyModelAssembler);
    }

    @Override
    public ResponseEntity<EntityModel<VacancyResponse>> createVacancy(@Valid VacancyRequest request) {
        VacancyResponse createdVacancy = vacancyService.createVacancy(request);
        EntityModel<VacancyResponse> entityModel = vacancyModelAssembler.toModel(createdVacancy);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<VacancyResponse> updateVacancy(Long id, @Valid VacancyRequest request) {
        VacancyResponse updatedVacancy = vacancyService.updateVacancy(id, request);
        return vacancyModelAssembler.toModel(updatedVacancy);
    }

    @Override
    public void deleteVacancy(Long id) {
        vacancyService.deleteVacancy(id);
    }
}
