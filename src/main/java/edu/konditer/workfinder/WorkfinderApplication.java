package edu.konditer.workfinder;

import edu.konditer.workfinder.service.UserService;
import edu.konditer.workfinder.service.VacancyService;
import edu.konditer.workfinder_contracts.dto.UserRequest;
import edu.konditer.workfinder_contracts.dto.VacancyRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication(
        scanBasePackages = {"edu.konditer.workfinder", "edu.konditer.workfinder_contracts"},
        exclude = { DataSourceAutoConfiguration.class }
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class WorkfinderApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkfinderApplication.class, args);
    }
}

@Component
class DataGen implements CommandLineRunner {
    private final UserService userService;
    private final VacancyService vacancyService;

    public DataGen(UserService userService, VacancyService vacancyService) {
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();

        int usersQty = 50;
        int vacanciesQty = 100;

        List<String> firstNames = List.of("Александр", "Иван", "Артем", "Андрей");
        List<String> lastNames = List.of("Иванов", "Андреев", "Никитин", "Михайлов");
        List<String> jobNames = List.of("frontend-developer", "fullstack-developer", "backend-developer", "devops-engineer");
        List<String> vacTitles1 = List.of("Very nice work | ", "We can purpose work | ", "The real work! | ");
        List<String> vacTitles2 = List.of("high salary!", "nice team", "well payed", "free cookies");
        List<String> vacTexts = List.of("We trying to fool some developers", "Free cookies and coffee");
        List<String> vacContactNumbers = List.of("7 (450) 321-21-21", "7 (450) 432-23-32");

        System.out.println("Creating users...");

        for (int i = 0; i < usersQty; i++) {
            List<String> featuredJobs = new ArrayList<>();

            for (int j = 0; j < 2; j++) {
                featuredJobs.add(jobNames.get(random.nextInt(jobNames.size())));
            }

            UserRequest user = new UserRequest(
                    firstNames.get(random.nextInt(firstNames.size())),
                    lastNames.get(random.nextInt(lastNames.size())),
                    featuredJobs
            );
            userService.createUser(user);
        }

        System.out.println("Creating vacancies...");

        for (int i = 0; i < vacanciesQty; i++) {
            VacancyRequest vacancy1 = new VacancyRequest(
                    vacTitles1.get(random.nextInt(vacTitles1.size())) + vacTitles2.get(random.nextInt(vacTitles2.size() - 1)),
                    vacTexts.get(random.nextInt(vacTexts.size())),
                    jobNames.get(random.nextInt(jobNames.size())),
                    vacContactNumbers.get(random.nextInt(vacContactNumbers.size())),
                    (double) random.nextInt(10, 100) * 100,
                    (long) random.nextInt(1, usersQty)
            );
            vacancyService.createVacancy(vacancy1);
        }

        System.out.println("DataGen completed.");
    }
}
