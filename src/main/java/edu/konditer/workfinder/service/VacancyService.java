package edu.konditer.workfinder.service;

import edu.konditer.events.VacancyCreatedEvent;
import edu.konditer.events.VacancyDeletedEvent;
import edu.konditer.workfinder.config.RabbitMQConfig;
import edu.konditer.workfinder.storage.InMemoryStorage;
import edu.konditer.workfinder_contracts.dto.PagedResponse;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.dto.VacancyRequest;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import edu.konditer.workfinder_contracts.exception.ResourceNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class VacancyService {
    private final InMemoryStorage storage;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    public VacancyService(InMemoryStorage storage, UserService userService, RabbitTemplate rabbitTemplate) {
        this.storage = storage;
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public VacancyResponse findVacancyById(Long id) {
        return Optional.ofNullable(storage.vacancies.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy", id));
    }

    public PagedResponse<VacancyResponse> findAllVacancies(Long userId, int page, int size) {
        Stream<VacancyResponse> vacancyResponseStream = storage.vacancies.values().stream()
                .sorted((v0, v1) -> v0.getId().compareTo(v1.getId()));

        if (userId != null) {
            vacancyResponseStream = vacancyResponseStream.filter(v -> v.getAuthor().getId().equals(userId));
        }

        List<VacancyResponse> vacancies = vacancyResponseStream.toList();

        int totalElements = vacancies.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<VacancyResponse> pageContent = (fromIndex > toIndex) ? List.of() : vacancies.subList(fromIndex, toIndex);
        return new PagedResponse<>(pageContent, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    public VacancyResponse createVacancy(VacancyRequest vacancyRequest) {
        UserResponse author = userService.findUserById(vacancyRequest.authorId());

        Long id = storage.vacancySequence.incrementAndGet();

        VacancyResponse vacancy = new VacancyResponse(
                id,
                vacancyRequest.title(),
                vacancyRequest.text(),
                vacancyRequest.jobName(),
                vacancyRequest.contactPhoneNumber(),
                vacancyRequest.salary(),
                LocalDateTime.now(),
                author
        );

        storage.vacancies.put(id, vacancy);

        VacancyCreatedEvent event = new VacancyCreatedEvent(
                vacancy.getId(),
                vacancy.getTitle(),
                author.getFirstName() + " " + author.getLastName(),
                vacancy.getJobName()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_VACANCY_CREATED, event);

        return vacancy;
    }

    public VacancyResponse updateVacancy(Long id, VacancyRequest vacancyRequest) {
        VacancyResponse existingVacancy = findVacancyById(id);

        UserResponse author = userService.findUserById(vacancyRequest.authorId());

        VacancyResponse updatedVacancy = new VacancyResponse(
                id,
                vacancyRequest.title(),
                vacancyRequest.text(),
                vacancyRequest.jobName(),
                vacancyRequest.contactPhoneNumber(),
                vacancyRequest.salary(),
                existingVacancy.getCreatedAt(),
                author
        );

        storage.vacancies.put(id, updatedVacancy);
        return updatedVacancy;
    }

    public void deleteVacancy(Long id) {
        VacancyResponse vacancy = findVacancyById(id);
        UserResponse author = vacancy.getAuthor();

        VacancyDeletedEvent event = new VacancyDeletedEvent(
                vacancy.getId()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_VACANCY_DELETED, event);

        storage.vacancies.remove(id);
    }

    public void deleteAllVacanciesByAuthorId(Long userId) {
        List<Long> vacanciesIdListToDelete = storage.vacancies.values().stream()
                .filter(v -> v.getAuthor().getId().equals(userId))
                .map(VacancyResponse::getId)
                .toList();

        vacanciesIdListToDelete.forEach(this::deleteVacancy);
    }
}
