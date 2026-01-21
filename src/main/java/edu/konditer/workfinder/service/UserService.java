package edu.konditer.workfinder.service;

import edu.konditer.workfinder.entity.User;
import edu.konditer.workfinder.mapper.UserMapper;
import edu.konditer.workfinder.repository.UserRepository;
import edu.konditer.workfinder_contracts.dto.UserRequest;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.exception.ResourceNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VacancyService vacancyService;

    public UserService(UserRepository userRepository, UserMapper userMapper, @Lazy VacancyService vacancyService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.vacancyService = vacancyService;
    }

    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toResponse(user);
    }

    public User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = findUserEntityById(id);
        userMapper.updateEntity(user, userRequest);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        findUserEntityById(id);
        vacancyService.deleteAllVacanciesByAuthorId(id);
        userRepository.deleteById(id);
    }
}
