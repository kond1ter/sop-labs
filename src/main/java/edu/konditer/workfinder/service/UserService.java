package edu.konditer.workfinder.service;

import edu.konditer.workfinder.storage.InMemoryStorage;
import edu.konditer.workfinder_contracts.dto.UserRequest;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final InMemoryStorage storage;
    private final VacancyService vacancyService;

    public UserService(InMemoryStorage storage, @Lazy VacancyService vacancyService) {
        this.storage = storage;
        this.vacancyService = vacancyService;
    }

    public UserResponse findUserById(Long id) {
        return Optional.ofNullable(storage.users.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public UserResponse createUser(UserRequest userRequest) {
        Long id = storage.userSequence.incrementAndGet();

        UserResponse user = new UserResponse(
                id,
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.featuredJobs()
        );

        storage.users.put(id, user);
        return user;
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        findUserById(id);

        UserResponse updatedUser = new UserResponse(
                id,
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.featuredJobs()
        );

        storage.users.put(id, updatedUser);
        return updatedUser;
    }

    public void deleteUser(Long id) {
        findUserById(id);
        vacancyService.deleteAllVacanciesByAuthorId(id);
        storage.users.remove(id);
    }
}
