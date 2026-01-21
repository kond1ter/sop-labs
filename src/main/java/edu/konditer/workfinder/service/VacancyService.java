package edu.konditer.workfinder.service;

import edu.konditer.events.VacancyCreatedEvent;
import edu.konditer.events.VacancyDeletedEvent;
import edu.konditer.workfinder.config.RabbitMQConfig;
import edu.konditer.workfinder.entity.User;
import edu.konditer.workfinder.entity.Vacancy;
import edu.konditer.workfinder.mapper.VacancyMapper;
import edu.konditer.workfinder.repository.VacancyRepository;
import edu.konditer.workfinder_contracts.dto.PagedResponse;
import edu.konditer.workfinder_contracts.dto.VacancyRequest;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import edu.konditer.workfinder_contracts.exception.ResourceNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VacancyService {
    private final VacancyRepository vacancyRepository;
    private final VacancyMapper vacancyMapper;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    public VacancyService(VacancyRepository vacancyRepository, VacancyMapper vacancyMapper, UserService userService, RabbitTemplate rabbitTemplate) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyMapper = vacancyMapper;
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public VacancyResponse findVacancyById(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy", id));
        return vacancyMapper.toResponse(vacancy);
    }

    public PagedResponse<VacancyResponse> findAllVacancies(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Vacancy> vacancyPage;

        if (userId != null) {
            vacancyPage = vacancyRepository.findByAuthorId(userId, pageable);
        } else {
            vacancyPage = vacancyRepository.findAll(pageable);
        }

        List<VacancyResponse> content = vacancyPage.getContent().stream()
                .map(vacancyMapper::toResponse)
                .toList();

        return new PagedResponse<>(
                content,
                vacancyPage.getNumber(),
                vacancyPage.getSize(),
                (int) vacancyPage.getTotalElements(),
                vacancyPage.getTotalPages(),
                vacancyPage.isLast()
        );
    }

    @Transactional
    public VacancyResponse createVacancy(VacancyRequest vacancyRequest) {
        User author = userService.findUserEntityById(vacancyRequest.authorId());

        Vacancy vacancy = new Vacancy(
                vacancyRequest.title(),
                vacancyRequest.text(),
                vacancyRequest.jobName(),
                vacancyRequest.contactPhoneNumber(),
                vacancyRequest.salary(),
                LocalDateTime.now(),
                author
        );

        Vacancy savedVacancy = vacancyRepository.save(vacancy);
        VacancyResponse vacancyResponse = vacancyMapper.toResponse(savedVacancy);

        VacancyCreatedEvent event = new VacancyCreatedEvent(
                vacancyResponse.getId(),
                vacancyResponse.getTitle(),
                author.getFirstName() + " " + author.getLastName(),
                vacancyResponse.getJobName()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_VACANCY_CREATED, event);

        return vacancyResponse;
    }

    @Transactional
    public VacancyResponse updateVacancy(Long id, VacancyRequest vacancyRequest) {
        Vacancy existingVacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy", id));

        User author = userService.findUserEntityById(vacancyRequest.authorId());

        existingVacancy.setTitle(vacancyRequest.title());
        existingVacancy.setText(vacancyRequest.text());
        existingVacancy.setJobName(vacancyRequest.jobName());
        existingVacancy.setContactNumber(vacancyRequest.contactPhoneNumber());
        existingVacancy.setSalary(vacancyRequest.salary());
        existingVacancy.setAuthor(author);

        Vacancy updatedVacancy = vacancyRepository.save(existingVacancy);
        return vacancyMapper.toResponse(updatedVacancy);
    }

    @Transactional
    public void deleteVacancy(Long id) {
        Vacancy vacancy = vacancyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vacancy", id));

        VacancyDeletedEvent event = new VacancyDeletedEvent(vacancy.getId());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_VACANCY_DELETED, event);

        vacancyRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllVacanciesByAuthorId(Long userId) {
        List<Vacancy> vacancies = vacancyRepository.findByAuthorId(userId, Pageable.unpaged()).getContent();
        vacancies.forEach(vacancy -> {
            VacancyDeletedEvent event = new VacancyDeletedEvent(vacancy.getId());
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_VACANCY_DELETED, event);
        });
        vacancyRepository.deleteAll(vacancies);
    }
}
