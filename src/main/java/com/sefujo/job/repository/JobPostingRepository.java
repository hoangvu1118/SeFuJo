package com.sefujo.job.repository;

import com.sefujo.job.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobPostingRepository extends JpaRepository<JobPosting,Long> {

}
