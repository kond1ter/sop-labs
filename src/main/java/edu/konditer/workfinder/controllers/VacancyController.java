package edu.konditer.workfinder.controllers;

import edu.konditer.workfinder_contracts.dto.PagedResponse;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.dto.VacancyRequest;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import edu.konditer.workfinder_contracts.endpoints.VacancyApi;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacancyController implements VacancyApi {
    @Override
    public VacancyResponse getVacancyById(Long id) {
        return null;
    }

    @Override
    public PagedResponse<VacancyResponse> getAllVacancies(int page, int size) {
        return null;
    }

    @Override
    public VacancyResponse createVacancy(@Valid VacancyRequest request) {
        return null;
    }

    @Override
    public UserResponse updateVacancy(Long id, @Valid VacancyRequest request) {
        return null;
    }

    @Override
    public void deleteVacancy(Long id) {

    }
}
