package com.sefujo.job.dto;

import com.sefujo.job.entity.Job;
import com.sefujo.searchprofile.EmploymentType;
import com.sefujo.searchprofile.WorkplaceType;
import lombok.Data;

import java.util.List;

@Data
public class JobSummaryResponse {

    private Long id;

    private String title;
    private String companyName;
    private String location;

    private String level;

    private EmploymentType employmentType;
    private WorkplaceType workplaceType;
}
