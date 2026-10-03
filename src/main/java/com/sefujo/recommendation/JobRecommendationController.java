package com.sefujo.recommendation;

import com.sefujo.recommendation.dto.JobRecommendationResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jobRecommendation")
@AllArgsConstructor
public class JobRecommendationController {
    JobRecommendationService jobRecommendationService;

    @PostMapping
    public ResponseEntity<JobRecommendationResponse> generateJobRecommendation() {
        JobRecommendationResponse jobRecommendationResponse = jobRecommendationService.generateRecommendations();
        return  ResponseEntity.ok(jobRecommendationResponse);
    }
}
