package edu.konditer.workfinder.mapper;

import edu.konditer.workfinder.entity.User;
import edu.konditer.workfinder_contracts.dto.UserRequest;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getFeaturedJobs()
        );
    }

    public User toEntity(UserRequest request) {
        return new User(
                request.firstName(),
                request.lastName(),
                request.featuredJobs()
        );
    }

    public void updateEntity(User user, UserRequest request) {
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setFeaturedJobs(request.featuredJobs());
    }
}

