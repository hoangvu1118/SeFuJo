package com.sefujo.recommendation.dto;

import com.sefujo.recommendation.JobRecommendation;
import lombok.Data;

import java.util.List;

@Data
public class JobRecommendationResponse {
    List<JobRecommendation> jobRecommendations;
}
