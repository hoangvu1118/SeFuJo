package com.sefujo.searchprofile;

import com.sefujo.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name="search_profiles")
public class SearchProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name="level")
    private String level;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ElementCollection
    @CollectionTable(
            name="search_profile_job_titles",
            joinColumns = @JoinColumn(name="search_profile_id")
    )
    @Column(name="job_title")
    private Set<String> jobTitles = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name="search_profile_skills",
            joinColumns = @JoinColumn(name="search_profile_id")
    )
    @Column(name="skill")
    private Set<String> skills = new HashSet<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name="search_profile_employment_types",
            joinColumns = @JoinColumn(name="search_profile_id")
    )
    @Column(name="employment_type")
    private Set<EmploymentType> employmentTypes = new HashSet<>();


    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "search_profile_workplace_types",
            joinColumns = @JoinColumn(name = "search_profile_id")
    )
    @Column(name = "workplace_type")
    private Set<WorkplaceType> workplaceTypes =
            new HashSet<>();
}
