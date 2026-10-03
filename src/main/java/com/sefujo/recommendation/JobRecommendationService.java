package com.sefujo.recommendation;

import com.sefujo.job.entity.Job;
import com.sefujo.recommendation.dto.JobRecommendationResponse;
import com.sefujo.searchprofile.SearchProfile;
import com.sefujo.searchprofile.SearchProfileService;
import com.sefujo.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class JobRecommendationService {
    private JobRecommendationRepository jobRecommendationRepository;
    private JobCandidateService jobCandidateService;
    private JobMatchingStrategy jobMatchingStrategy;
    private UserService userService;
    private SearchProfileService searchProfileService;

    private JobRecommendation persistRecommendation(SearchProfile profile, MatchResult matchResult, Job job) {
        JobRecommendation jobRecommendation = jobRecommendationRepository
                        .findBySearchProfileAndJob(profile, job)
                        .orElseGet(() -> {
                            JobRecommendation newRecommendation =
                                    new JobRecommendation();

                            newRecommendation.setSearchProfile(profile);
                            newRecommendation.setJob(job);
                            newRecommendation.setStatus(
                                    RecommendationStatus.NEW
                            );
                            newRecommendation.setCreatedAt(
                                    LocalDateTime.now()
                            );

                            return newRecommendation;
                        });
        jobRecommendation.setMatchReasons(matchResult.getReasons());
        jobRecommendation.setMatchScore(matchResult.getScore());
        jobRecommendation.setUpdatedAt(LocalDateTime.now());
        return jobRecommendationRepository.save(jobRecommendation);
    }

    public JobRecommendationResponse generateRecommendations() {
        long userId = userService.getCurrentUserId();
        SearchProfile searchProfile = searchProfileService.getSearchProfileByUSerID(userId);
        List<Job> jobs = jobCandidateService.findCandidates(searchProfile);

        List<JobRecommendation> jobRecommendationResponseList = new ArrayList<>();
        for(Job job : jobs){
            MatchResult result = jobMatchingStrategy.score(searchProfile, job);


            if(result.getScore() >= 20){
                JobRecommendation jobRcm = persistRecommendation(searchProfile, result, job);
                jobRecommendationResponseList.add(jobRcm);
            }
        }
        JobRecommendationResponse response = new JobRecommendationResponse();
        response.setJobRecommendations(jobRecommendationResponseList);
        return response;
    }
}
