package com.sefujo.searchprofile.dto;

import com.sefujo.searchprofile.EmploymentType;
import com.sefujo.searchprofile.WorkplaceType;
import lombok.Data;

import java.util.Set;

@Data
public class SearchProfileResponse {
    private Long Id;
    private String name;
    private String level;
    private Set<String> jobTitles;
    private Set<String> skills;
    private Set<String> locations;
    private Set<EmploymentType> employmentTypes;
    private Set<WorkplaceType> workplaceTypes;
}
