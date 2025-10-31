package edu.konditer.workfinder.graphql;

import com.netflix.graphql.dgs.*;
import edu.konditer.workfinder.service.VacancyService;
import edu.konditer.workfinder_contracts.dto.PagedResponse;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.dto.VacancyRequest;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import graphql.schema.DataFetchingEnvironment;

import java.util.Map;

@DgsComponent
public class VacancyDataFetcher {

    private final VacancyService vacancyService;

    public VacancyDataFetcher(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @DgsQuery
    public VacancyResponse vacancyById(@InputArgument Long id) {
        return vacancyService.findVacancyById(id);
    }

    @DgsQuery
    public PagedResponse<VacancyResponse> vacanciesPage(@InputArgument Long userId, @InputArgument int page, @InputArgument int size) {
        return vacancyService.findAllVacancies(userId, page, size);
    }

    @DgsData(parentType = "Vacancy", field = "author")
    public UserResponse author(DataFetchingEnvironment dfe) {
        VacancyResponse vacancy = dfe.getSource();
        return vacancy.getAuthor();
    }

    @DgsMutation
    public VacancyResponse createVacancy(@InputArgument("input") Map<String, Object> input) {
        VacancyRequest request = new VacancyRequest(
                (String) input.get("title"),
                (String) input.get("text"),
                (String) input.get("jobName"),
                (String) input.get("contactNumber"),
                Double.parseDouble(input.get("salary").toString()),
                Long.parseLong(input.get("authorId").toString())
        );
        return vacancyService.createVacancy(request);
    }

    @DgsMutation
    public VacancyResponse updateVacancy(@InputArgument Long id, @InputArgument("input") Map<String, Object> input) {
        VacancyRequest request = new VacancyRequest(
                (String) input.get("title"),
                (String) input.get("text"),
                (String) input.get("jobName"),
                (String) input.get("contactNumber"),
                Double.parseDouble(input.get("salary").toString()),
                Long.parseLong(input.get("authorId").toString())
        );
        return vacancyService.updateVacancy(id, request);
    }

    @DgsMutation
    public Long deleteVacancy(@InputArgument Long id) {
        vacancyService.deleteVacancy(id);
        return id;
    }
}

