package com.sefujo.job.dto;

import com.sefujo.searchprofile.EmploymentType;
import com.sefujo.searchprofile.WorkplaceType;
import lombok.Data;

@Data
public class JobDetailResponse {

    private Long id;

    private String title;
    private String companyName;
    private String companyWebsite;

    private String location;
    private String level;

    private EmploymentType employmentType;
    private WorkplaceType workplaceType;

    private String description;
    private String status;
}