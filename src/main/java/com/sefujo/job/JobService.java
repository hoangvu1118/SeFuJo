package com.sefujo.job;

import com.sefujo.common.exception.ResourceNotFound;
import com.sefujo.job.dto.JobDetailResponse;
import com.sefujo.job.dto.JobSummaryResponse;
import com.sefujo.job.entity.Company;
import com.sefujo.job.entity.Job;
import com.sefujo.job.repository.CompanyRepository;
import com.sefujo.job.repository.JobRepository;
import com.sefujo.job.repository.JobSpecification;
import com.sefujo.job.source.greenhouse.GreenhouseLocation;
import com.sefujo.searchprofile.EmploymentType;
import com.sefujo.searchprofile.WorkplaceType;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

    public @Nullable List<JobSummaryResponse> getJobs(String title, EmploymentType employmentType, WorkplaceType workplaceType, String location) {
        Specification<Job> spec =
                Specification.where(JobSpecification.isOpen())
                        .and(JobSpecification.hasTitle(title))
                        .and(JobSpecification.hasEmploymentType(employmentType))
                        .and(JobSpecification.hasWorkplaceType(workplaceType))
                        .and(JobSpecification.hasLocation(location));

        Sort sort = Sort.by(
                Sort.Direction.DESC,
                "createdDate"
        );


        List<Job> jobs = jobRepository.findAll(spec,  sort);
        List<JobSummaryResponse> jobResponses = new ArrayList<>();
        for (Job job : jobs) {
            JobSummaryResponse jobResponse = new JobSummaryResponse();
            jobResponse.setId(job.getId());
            jobResponse.setCompanyName(job.getCompany().getName());
            jobResponse.setLevel(job.getLevel());
            jobResponse.setTitle(job.getTitle());
            jobResponse.setEmploymentType(job.getEmploymentType());
            jobResponse.setWorkplaceType(job.getWorkplaceType());
            jobResponse.setLocation(job.getLocation());
            jobResponses.add(jobResponse);
        }
        return jobResponses;
    }

    public JobDetailResponse getJobDetails(long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Job not found"));
        JobDetailResponse response = new JobDetailResponse();
        response.setId(job.getId());
        response.setCompanyName(job.getCompany().getName());
        response.setCompanyWebsite(job.getCompany().getWebsite());
        response.setLevel(job.getLevel());
        response.setTitle(job.getTitle());
        response.setEmploymentType(job.getEmploymentType());
        response.setWorkplaceType(job.getWorkplaceType());
        response.setLocation(job.getLocation());
        response.setDescription(job.getDescription());
        response.setStatus(job.getStatus());
        return response;
    }



}
