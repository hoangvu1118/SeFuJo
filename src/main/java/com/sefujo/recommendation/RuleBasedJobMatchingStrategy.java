package com.sefujo.recommendation;

import com.sefujo.job.entity.Job;
import com.sefujo.searchprofile.SearchProfile;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class RuleBasedJobMatchingStrategy implements JobMatchingStrategy {

    private boolean matchesTitle(
            SearchProfile profile,
            Job job
    ) {
        if (profile.getJobTitles() == null ||
                profile.getJobTitles().isEmpty() ||
                job.getNormalizedTitle() == null) {
            return false;
        }

        String jobTitle =
                job.getNormalizedTitle().toLowerCase(Locale.ROOT);

        return profile.getJobTitles().stream()
                .map(title -> title.toLowerCase(Locale.ROOT))
                .anyMatch(title ->
                        jobTitle.contains(title) ||
                                title.contains(jobTitle)
                );
    }

    private boolean matchesLocation(
            SearchProfile profile,
            Job job
    ) {
        if (profile.getLocations() == null ||
                profile.getLocations().isEmpty() ||
                job.getLocation() == null) {
            return false;
        }

        String jobLocation =
                job.getLocation().toLowerCase(Locale.ROOT);

        return profile.getLocations().stream()
                .map(location ->
                        location.toLowerCase(Locale.ROOT))
                .anyMatch(jobLocation::contains);
    }

    private boolean matchesEmploymentType(
            SearchProfile profile,
            Job job
    ) {
        return profile.getEmploymentTypes() != null
                && !profile.getEmploymentTypes().isEmpty()
                && job.getEmploymentType() != null
                && profile.getEmploymentTypes()
                .contains(job.getEmploymentType());
    }

    private boolean matchesSkills(
            SearchProfile profile,
            Job job
    ) {
        if (profile.getSkills() == null ||
                profile.getSkills().isEmpty() ||
                job.getDescription() == null) {
            return false;
        }

        String description =
                job.getDescription().toLowerCase(Locale.ROOT);

        return profile.getSkills().stream()
                .map(skill -> skill.toLowerCase(Locale.ROOT))
                .anyMatch(description::contains);
    }



    @Override
    public MatchResult score(SearchProfile profile, Job job) {
        int score = 0;
        Set<MatchReason> reasons = EnumSet.noneOf(MatchReason.class); // to initialize it, either empty or add later
        if (matchesTitle(profile, job)) {
            score += 40;
            reasons.add(MatchReason.TITLE_MATCH);
        }
        if (matchesLocation(profile, job)) {
            score += 20;
            reasons.add(MatchReason.LOCATION_MATCH);
        }

        if (matchesEmploymentType(profile, job)) {
            score += 20;
            reasons.add(MatchReason.EMPLOYMENT_TYPE_MATCH);
        }

        if (matchesSkills(profile, job)) {
            score += 20;
            reasons.add(MatchReason.SKILL_MATCH);
        }

        return new MatchResult(score, reasons);

    }

}
