package com.sefujo.recommendation;

import com.sefujo.job.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Set;

@Data
@AllArgsConstructor
public class MatchResult {
    int score;
    Set<MatchReason> reasons ;
}
