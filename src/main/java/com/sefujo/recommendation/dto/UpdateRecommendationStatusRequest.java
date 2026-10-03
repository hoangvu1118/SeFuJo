package com.sefujo.recommendation.dto;

import com.sefujo.recommendation.RecommendationStatus;
import lombok.Data;

@Data
public class UpdateRecommendationStatusRequest {
    public long jobRecommendationId;
    public RecommendationStatus recommendationStatus;
}
