package com.sefujo.job.repository;

import com.sefujo.job.entity.Job;
import com.sefujo.searchprofile.EmploymentType;
import com.sefujo.searchprofile.WorkplaceType;
import org.springframework.data.jpa.domain.Specification;

// Specification reduces the amount of COMBINATION written in Repository
// Query : the overall query
// CB: CriteriaBuilder : create conditions like =, Like, AND, OR

public class JobSpecification {
    public static Specification<Job> isOpen() {
        return (root, query, cb) ->
                cb.equal(root.get("status"), "OPEN");
    }
    public static Specification<Job> hasTitle(String title) {
        return (root, query, cb) ->
                title == null
                        ? cb.conjunction() // means 'TRUE', accept to return TRUE
                        : cb.like(
                        cb.lower(root.get("normalizedTitle")),
                        "%" + title.toLowerCase() + "%"
                );
    }

    public static Specification<Job> hasEmploymentType(
            EmploymentType employmentType
    ) {
        return (root, query, cb) ->
                employmentType == null
                        ? cb.conjunction()
                        : cb.equal(
                        root.get("employmentType"),
                        employmentType
                );
    }

    public static Specification<Job> hasWorkplaceType(
            WorkplaceType workplaceType
    ) {
        return (root, query, cb) ->
                workplaceType == null
                        ? cb.conjunction()
                        : cb.equal(
                        root.get("workplaceType"),
                        workplaceType
                );
    }

    public static Specification<Job> hasLocation(String location) {
        return (root, query, cb) ->
                location == null
                        ? cb.conjunction()
                        : cb.like(
                        cb.lower(root.get("location")),
                        "%" + location.toLowerCase() + "%"
                );
    }
}