package edu.konditer.workfinder.mapper;

import edu.konditer.workfinder.entity.Vacancy;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import org.springframework.stereotype.Component;

@Component
public class VacancyMapper {
    private final UserMapper userMapper;

    public VacancyMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public VacancyResponse toResponse(Vacancy vacancy) {
        UserResponse authorResponse = userMapper.toResponse(vacancy.getAuthor());
        return new VacancyResponse(
                vacancy.getId(),
                vacancy.getTitle(),
                vacancy.getText(),
                vacancy.getJobName(),
                vacancy.getContactNumber(),
                vacancy.getSalary(),
                vacancy.getCreatedAt(),
                authorResponse
        );
    }
}

