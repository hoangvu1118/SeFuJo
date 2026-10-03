package com.sefujo.recommendation;

import com.sefujo.job.entity.Job;
import com.sefujo.searchprofile.SearchProfile;

public interface JobMatchingStrategy {
    public MatchResult score(SearchProfile profile, Job job);
}
