package com.sefujo.recommendation;

import com.sefujo.job.entity.Job;
import com.sefujo.searchprofile.SearchProfile;

import java.util.List;

public interface JobCandidateService {
    List<Job> findCandidates(SearchProfile profile);
}
