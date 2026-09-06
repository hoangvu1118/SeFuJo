package com.sefujo.job.controller;

import com.sefujo.job.JobService;
import com.sefujo.job.dto.JobDetailResponse;
import com.sefujo.job.dto.JobSummaryResponse;
import com.sefujo.job.entity.Job;
import com.sefujo.searchprofile.EmploymentType;
import com.sefujo.searchprofile.WorkplaceType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/job")
@AllArgsConstructor
public class JobController {
    JobService jobService;

    @GetMapping()
    public ResponseEntity<List<JobSummaryResponse>> getJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) EmploymentType employmentType,
            @RequestParam(required = false) WorkplaceType workplaceType,
            @RequestParam(required = false) String location
    ) {
        return ResponseEntity.ok(
                jobService.getJobs(title, employmentType, workplaceType, location)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<JobDetailResponse> getJobDetails(@PathVariable long id) {
        JobDetailResponse jobDetailResponse = jobService.getJobDetails(id);
        return ResponseEntity.ok(jobDetailResponse);
    }
}
