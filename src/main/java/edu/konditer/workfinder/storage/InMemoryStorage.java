package edu.konditer.workfinder.storage;

import edu.konditer.workfinder.service.UserService;
import edu.konditer.workfinder.service.VacancyService;
import edu.konditer.workfinder_contracts.dto.UserResponse;
import edu.konditer.workfinder_contracts.dto.VacancyResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, UserResponse> users = new ConcurrentHashMap<>();
    public final Map<Long, VacancyResponse> vacancies = new ConcurrentHashMap<>();

    public final AtomicLong userSequence = new AtomicLong(0);
    public final AtomicLong vacancySequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
//        long user1Id = userSequence.incrementAndGet();
//        long user2Id = userSequence.incrementAndGet();
//
//        long vacancy1Id = vacancySequence.incrementAndGet();
//        long vacancy2Id = vacancySequence.incrementAndGet();
//
//        VacancyResponse vacancy1 = new VacancyResponse(vacancy1Id, "Backend developer | high salary!", "We trying to fool some developers", "backend-developer", "7 (450) 321-21-21", 9999.99, LocalDateTime.now().minusDays(10), user1Id, "Иван Иванов");
//        VacancyResponse vacancy2 = new VacancyResponse(vacancy1Id, "Senior Frontend developer", "Free cookies and coffee", "frontend-developer", "7 (450) 432-23-32", 5500.00, LocalDateTime.now().minusDays(10), user2Id, "Иван Иванов");
//
//        UserResponse user1 = new UserResponse(user1Id, "Иван", "Иванов", new ArrayList<>(List.of("frontend-developer", "fullstack-developer")), new ArrayList<>(List.of(vacancy1)));
//        UserResponse user2 = new UserResponse(user2Id, "Владимир", "Владимирович", new ArrayList<>(List.of("backend-developer")), new ArrayList<>(List.of(vacancy2)));
//
//        vacancies.put(vacancy1Id, vacancy1);
//        vacancies.put(vacancy2Id, vacancy2);
//
//        users.put(user1.getId(), user1);
//        users.put(user2.getId(), user2);
    }
}