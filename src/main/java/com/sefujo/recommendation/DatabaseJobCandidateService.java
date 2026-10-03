package com.sefujo.recommendation;

import com.sefujo.job.entity.Job;
import com.sefujo.job.repository.JobRepository;
import com.sefujo.job.repository.JobSpecification;
import com.sefujo.searchprofile.SearchProfile;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseJobCandidateService implements JobCandidateService {
    private JobRepository jobRepository;

    @Override
    public List<Job> findCandidates(SearchProfile profile) {
        // Use JPA Specification because some fields are optional in SearchProfile
        // -> Filter Dynamically works best with JPA Specification
        Specification<Job> spec = JobSpecification.isOpen();

//        if (!profile.getEmploymentTypes().isEmpty()) {
//            spec = spec.and(
//                    JobSpecification.hasEmploymentTypes(
//                            profile.getEmploymentTypes()
//                    )
//            );
//        }
//        if (!profile.getWorkplaceTypes().isEmpty()) {
//            spec = spec.and(
//                    JobSpecification.hasWorkplaceTypes(
//                            profile.getWorkplaceTypes()
//                    )
//            );
//        }

        if (!profile.getLocations().isEmpty()) {
            spec = spec.and(
                    JobSpecification.hasAnyLocation(
                            profile.getLocations()
                    )
            );
        }
        return jobRepository.findAll(spec);
    }
}

// The findCandidate is a filter
// WHERE status = 'OPEN'
//
//AND employment_type IN ('INTERNSHIP')
//
//AND workplace_type IN ('HYBRID', 'ONSITE')
//
//AND (
//    LOWER(location) LIKE '%ho chi minh city%'
//    OR LOWER(location) LIKE '%hanoi%'
//)