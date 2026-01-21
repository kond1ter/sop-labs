package edu.konditer.workfinder.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_featured_jobs", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "job_name")
    private List<String> featuredJobs = new ArrayList<>();

    public User() {
    }

    public User(String firstName, String lastName, List<String> featuredJobs) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.featuredJobs = featuredJobs != null ? featuredJobs : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getFeaturedJobs() {
        return featuredJobs;
    }

    public void setFeaturedJobs(List<String> featuredJobs) {
        this.featuredJobs = featuredJobs != null ? featuredJobs : new ArrayList<>();
    }
}

